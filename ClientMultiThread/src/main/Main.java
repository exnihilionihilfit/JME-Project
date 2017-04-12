package main;

import Control.InputListener;
import model.MyWayList;
import model.Entity;
import Control.InputServerData;
import Control.ServerConnection;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;

import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private static Main app = null;
    private static InputServerData serverConection;

    MyWayList wayList = null;
    Future future = null;
    public Client myClient = null;
    long lastTime = System.currentTimeMillis();
    private static List<Entity> entities = null;
    /* This constructor creates a new executor with a core pool size of 4. */
    public ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50);

    private static String[] args;

    public static void main(String[] args) {

        Main.args = args;

            System.out.println("Starting Client");
            app = new Main();
            app.start(JmeContext.Type.Display); // standard display type
      

    }

    @Override
    public void simpleInitApp() {

        entities = new ArrayList();

        createEntity();
        initKeys();
        connectToServer();
        addMessageListener();
        startClient();

    }

    @Override
    public void simpleUpdate(float tpf) {

        entities.forEach((entity) -> {
            entity.update(tpf);
        });

        if (!myClient.isConnected()) {
            try {
                System.out.println(" ... disconnected from Server try reconect in 5 Seconds ...");
                connectToServer();
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(InputListener.IS_ROTATE_PRESSED);
        InputListener.resetInput();

    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        // Add the names to the action listener.   
        inputManager.addListener(InputListener.KEY_INPUT_LISTENER, "Left", "Right", "Rotate", "Pause");

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

        entities.add(man);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void destroy() {

        if (myClient != null) {
            myClient.close();
        }

        super.destroy();
        executor.shutdown();
    }

    private void addMessageListener() {
        myClient.addMessageListener(new ClientListener(), PingMessage.class);
        myClient.addMessageListener(new ClientListener(), createEntityMessage.class);
        myClient.addMessageListener(new ClientListener(), PositionMessage.class);
    }

    private void connectToServer() {

        serverConection = new InputServerData(Main.args);
        ServerConnection serverConnection = new ServerConnection(this);
        serverConnection.connectToServer();
        

    }

    private void startClient() {
        if (myClient != null) {
            myClient.start();

        }
    }

    @Serializable
    public static class PingMessage extends AbstractMessage {

        private String hello;       // custom message data
        private long time;

        public PingMessage() {
        }    // empty constructor

        public PingMessage(String s, long t) {
            hello = s;
            time = t;
        } // custom constructor
    }

    @Serializable
    public static class PositionMessage extends AbstractMessage {

        private String position;
        private String entityID;

        public PositionMessage() {

        }

        public PositionMessage(String position, String entityID) {
            this.position = position;
            this.entityID = entityID;
        }
    }

    @Serializable
    public static class createEntityMessage extends AbstractMessage {

        private boolean isNewEntity = false;

        public createEntityMessage() {

        }

        public createEntityMessage(boolean isNewEntity) {
            this.isNewEntity = isNewEntity;
        }
    }

    public class ClientListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message message) {
            if (message instanceof PingMessage) {
                // do something with the message
                PingMessage helloMessage = (PingMessage) message;
                System.out.println("Client #" + source.getId() + " received: '" + helloMessage.hello + "'");
            }

            if (message instanceof createEntityMessage) {
                createEntityMessage createEntityMessage = (createEntityMessage) message;
                System.out.println(createEntityMessage.isNewEntity);
            }

            if (message instanceof PositionMessage) {
                PositionMessage positionMessage = (PositionMessage) message;

                for (Entity entity : entities) {
                    if (entity.getID() == Integer.parseInt(positionMessage.entityID)) {
                        entity.setRecevedNewPositionMessage(true);
                        entity.setNextPosition(positionMessage.position);
                        System.out.println("reseved new Position" + positionMessage.position);
                    }
                }

            }

        }
    }

}
