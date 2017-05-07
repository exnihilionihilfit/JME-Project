package main;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import control.GameState;
import control.InputListener;
import control.InputServerData;
import control.Map;
import control.UpdateEntity;
import control.network.NetworkMessageHandling;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;
import control.network.SendNetworkMessage;

import control.network.ServerConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Entity;
import model.EntityContainer;
import model.MyWayList;
import model.Player;
import org.lwjgl.opengl.Display;
import view.HUD;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private static Main mainApplication = null;
    private static InputServerData inputServerConection;

    MyWayList wayList = null;
    Future future = null;
    public Client client = null;
    long lastTime = System.currentTimeMillis();
    private static final List<Entity> ENTITIES = new ArrayList();
    /* This constructor creates a new executor with a core pool size of 4. */
    public ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    public SendNetworkMessage sendNetworkMessage;

    private static String[] args;
    private boolean isRunning;
    private GameState gameState;

    public final static Vector3f CAMERA_START_POSITION = new Vector3f(0, 150, -130);

    private Map map;
    // get width and height from OpenGl directly in init
    public int screenWidth = 0;
    public int screenHeight = 0;

    private HUD hud;
    private final long REGISTER_ON_SERVER_DELAY = 1000;

    public static AmbientLight entityHighLightLight = new AmbientLight(ColorRGBA.Green);
    public static AmbientLight entityNeutralHighLightLight = new AmbientLight(ColorRGBA.Orange);

    public static void main(String[] args) {

        Main.args = args;
        mainApplication = new Main();

        AppSettings newSetting = new AppSettings(true);

        newSetting.setFrameRate(60);
        newSetting.setMinResolution(1024, 860);

        mainApplication.setSettings(newSetting);

        mainApplication.start(JmeContext.Type.Display); // standard display type

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void simpleInitApp() {
//stateManager.detach( stateManager.getState(FlyCamAppState.class) );
        /**
         * Set up Physics
         */
        BulletAppState bulletAppState = new BulletAppState();
        //  stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        //   bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));

        getCamera().lookAtDirection(new Vector3f(0, -1, 0.70f), Vector3f.UNIT_Z);
        getCamera().setLocation(this.CAMERA_START_POSITION);

        // Make the main window resizable
        Display.setResizable(true);
        // Prevent the flycam from grabbing the mouse
        // This is necessary because otherwise we couldn't resize the window.
        //   flyCam.setDragToRotate(true);

        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);

        // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -1.7f, -1.0f));
        rootNode.addLight(sun);

        screenWidth = Math.max(Display.getWidth(), 1);
        screenHeight = Math.max(Display.getHeight(), 1);

        bulletAppState.setDebugEnabled(true);

        initKeys();
        initGameState();
        initHUD();
        initMap();

        isRunning = true;

    }

    @Override
    public void simpleUpdate(float tpf) {

        checkIfDisplayIsResized();
        
        if (HUD.IS_SERVER_ADRESS_ENTERD) {
            connectToServer();
            if (client != null) {
                addMessageListener();
                initSendNetworkMessage();
                startClient();

                // wait as long a id is given by server
                while (Player.getPlayerId() == 0L) {
                    registerOnServer();
                    System.out.println("try to register on server");
                    try {
                        Thread.sleep(REGISTER_ON_SERVER_DELAY);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                System.out.println(" client registered ");
                HUD.IS_SERVER_ADRESS_ENTERD = false;
            }
        } else if (client != null && client.isConnected()) {

            UpdateEntity.update(ENTITIES, tpf);

            checkServerConnection();

            // update current gamestate
            gameState.updateGameState();
            // check Action on gameState
            gameState.entityInteraction();
            // check HUD input action
            gameState.hudInput();
            // mouse zoom
            gameState.mouseZoom();
            // move camera
            gameState.moveCamera();

            // Reset all Key and Mouse Inputstatets
            InputListener.resetInput();
            // HUD reset e.g. buttons
            hud.resetInput();
            // Handle messages from server 
            NetworkMessageHandling.handlePingMessage();
            NetworkMessageHandling.handleEntitiesListMessage(this);

            

        }
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("ResetCamera", new KeyTrigger(KeyInput.KEY_SPACE));
        // Add the names to the action listener.   
        inputManager.addListener(InputListener.ACTION_LISTENER, "Left", "Right", "ResetCamera", "Pause");

        inputManager.addMapping("LeftMouse", new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "LeftMouse");

        inputManager.addMapping("RightMouse", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "RightMouse");

        inputManager.addMapping("MouseWheelForward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "MouseWheelForward");

        inputManager.addMapping("MouseWheelBackward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "MouseWheelBackward");

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void destroy() {

        if (client != null) {
            client.close();
        }

        super.destroy();
        executor.shutdown();
    }

    private void addMessageListener() {

        if (client != null) {
            NetworkMessageListener networkMessageListener = new NetworkMessageListener();
            client.addMessageListener(networkMessageListener.new ClientListener(),
                    NetworkMessages.PingMessage.class,
                    NetworkMessages.CreateEntityMessage.class,
                    NetworkMessages.EntityPositionMessage.class,
                    NetworkMessages.RegisterOnServerMessage.class,
                    NetworkMessages.EntitiesListMessage.class,
                    EntityContainer.class);

            client.addMessageListener(networkMessageListener.new ClientMoveOrderListener(),
                    NetworkMessages.EntityPositionMessage.class);
        }

    }

    private void connectToServer() {

        //inputServerConection = new InputServerData(Main.args);
        ServerConnection serverConnection = new ServerConnection(this);

        if (InputServerData.IS_VALID_SERVER_DATA) {
            serverConnection.connectToServer();
        }
    }

    private void startClient() {
        if (client != null) {
            client.start();
        }
    }

    private void initHUD() {
        hud = new HUD(mainApplication, mainApplication.guiNode, mainApplication.guiFont, mainApplication.assetManager);
    }

    public static List<Entity> getEntities() {
        return ENTITIES;
    }

    /**
     * Get ContainerEntities as a List and search firstly for entities to update
     * if an entity is not found its a new entity and will be added to the local
     * entity list
     *
     * @param containerEntities
     */
    public void setEntitiesPositionFromServerList(ArrayList<EntityContainer> containerEntities) {

        boolean found = false;
        
        

        for (EntityContainer entityContainer : containerEntities) {
            for (Entity entity : Main.ENTITIES) {
                found = false;

                if (entity.getID() == entityContainer.entityId) {

                    entity.setDirection(entityContainer.direction);
                    entity.setPosition(entityContainer.position);
                    found = true;
                    break;
                }
                
                
            }
            if (!found) {
                
            
                if(entityContainer.entityId >= -1)
                {
                    System.out.println("create new Entity geometry "+entityContainer.type);
                createEntity(entityContainer);
               
                
                }
            }

        }

    }

    private void createEntity(EntityContainer entityContainer) {

        Entity entity = new Entity(mainApplication, assetManager, entityContainer);
        
        

        entity.setPosition(entityContainer.position);
       if(entity.getEntityNode() != null){
             rootNode.attachChild(entity.getEntityNode());

            ENTITIES.add(entity);
       }
       else
       {
           System.out.println("Crete new Entity failed for type: "+entityContainer.type);
       }
          
        
    }

    private void initSendNetworkMessage() {
        sendNetworkMessage = new SendNetworkMessage(client);
    }

    private void checkServerConnection() {

        if (client == null) {
            System.out.println(" can't connect to server (offline?) ");
        } else if (!client.isConnected()) {
            try {
                System.out.println(" ... disconnected from Server try reconect in 5 Seconds");
                connectToServer();
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void initGameState() {
        gameState = new GameState(this, inputManager, rootNode, cam);
    }

    private void initMap() {
        map = new Map();
        map.initSky(assetManager, rootNode);
        rootNode.attachChild(map.makeFloor(assetManager));
    }

    private void checkIfDisplayIsResized() {
        if (Display.wasResized() || screenWidth == 0 || screenHeight == 0) {
            screenWidth = Math.max(Display.getWidth(), 1);
            screenHeight = Math.max(Display.getHeight(), 1);
            HUD.UPDATE_GUI = true;
            reshape(screenWidth, screenHeight);
        }
        
        if(HUD.UPDATE_GUI)
        {
            hud.updateMenu(guiNode);
        }
    }

    private void registerOnServer() {
        try {
            client.send(new NetworkMessages.RegisterOnServerMessage("Bob"));
        } catch (Exception e) {
            System.out.println(" client is not ready ");
        }

    }

}
