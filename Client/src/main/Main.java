package main;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.renderer.RenderManager;
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
    public ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50);

    public SendNetworkMessage sendNetworkMessage;

    private static String[] args;

    private boolean isRunning;

    private GameState gameState;

    private final Vector3f camerPosition = new Vector3f(0, 50, -30);

    private Map map;
    // get width and height from OpenGl directly in init
    private int screenWidth = 0;
    private int screenHeight = 0;
    
        private HUD hud;
    private long REGISTER_ON_SERVER_DELAY = 1000;

    public static void main(String[] args) {

        Main.args = args;

        mainApplication = new Main();
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
        getCamera().setLocation(this.camerPosition);

        // Make the main window resizable
        Display.setResizable(true);
        // Prevent the flycam from grabbing the mouse
        // This is necessary because otherwise we couldn't resize the window.
        flyCam.setDragToRotate(true);

        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);

        // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        screenWidth = Math.max(Display.getWidth(), 1);
        screenHeight = Math.max(Display.getHeight(), 1);

        bulletAppState.setDebugEnabled(true);

        connectToServer();
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
        
       
        initKeys();
        initGameState();
        initHUD();
        initMap();
        createEntity();

        isRunning = true;

    }

    @Override
    public void simpleUpdate(float tpf) {

        UpdateEntity.update(ENTITIES, tpf);

        checkServerConnection();

        // update current gamestate
        gameState.updateGameState();
        // check Action on gameState
        gameState.entityInteraction();
        // check HUD input action
        gameState.hudInput();

        // Reset all Key and Mouse Inputstatets
        InputListener.resetInput();
        // HUD reset e.g. buttons
        hud.resetInput();
        // Handle messages from server 
        NetworkMessageHandling.handlePingMessage();
        NetworkMessageHandling.handleEntityPositionMessage();

        checkIfDisplayIsResized();

    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        // Add the names to the action listener.   
        inputManager.addListener(InputListener.ACTION_LISTENER, "Left", "Right", "Rotate", "Pause");

        inputManager.addMapping("LeftMouse", new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "LeftMouse");

        inputManager.addMapping("RightMouse", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "RightMouse");

        inputManager.addMapping("MouseWheelForward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "MouseWheelForward");

        inputManager.addMapping("MouseWheelBackward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(InputListener.MOUSE_INPUT_LISTENER, "MouseWheelBackward");

    }

    private void createEntity() {

        Entity man = new Entity(this, this.assetManager, "carl");

        rootNode.attachChild(man.getEntity());

        ENTITIES.add(man);

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
                    NetworkMessages.RegisterOnServer.class,
                    NetworkMessages.EntitiesListMessage.class,
                    EntityContainer.class);

        }

    }

    private void connectToServer() {

        inputServerConection = new InputServerData(Main.args);
        ServerConnection serverConnection = new ServerConnection(this);

        if (inputServerConection.isValidServerData()) {
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
        rootNode.attachChild(map.makeFloor(assetManager));
    }

    private void checkIfDisplayIsResized() {
        if (Display.wasResized() || screenWidth == 0 || screenHeight == 0) {
            screenWidth = Math.max(Display.getWidth(), 1);
            screenHeight = Math.max(Display.getHeight(), 1);
            hud.updateMenu();
            reshape(screenWidth, screenHeight);
        }
    }

    private void registerOnServer() {
        try{
            client.send(new NetworkMessages.RegisterOnServer("Bob"));
        }catch(Exception e)
        {
            System.out.println(" client is not ready ");
        }
        
    }

}
