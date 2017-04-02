package mygame;

import Control.ServerConection;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;

import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;
import java.io.IOException;
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
    private static ServerConection serverConection;

    MyWayList wayList = null;
    Future future = null;
    Client myClient = null;
    long lastTime = System.currentTimeMillis();
    


    public static void main(String[] args) {

        serverConection = new ServerConection(args);

        if (serverConection.isValidServerData()) {
            System.out.println("Starting Client");
            app = new Main();
            app.start(JmeContext.Type.Display); // standard display type
        }

    }

    private static List<Entity> entities = null;
    /* This constructor creates a new executor with a core pool size of 4. */
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50);

    @Override
    public void simpleInitApp() {

        entities = new ArrayList();

        createEntity();
        initKeys();
        connectToServer();

   
    }
    
        @Override
    public void simpleUpdate(float tpf) {

        entities.forEach((entity) -> {
            entity.update(tpf);
        });
        
        if(!myClient.isConnected())
        {
            try {
                System.out.println(" ... disconnected from Server try reconect in 5 Seconds ...");
                connectToServer();
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void initKeys() {
        inputManager.addMapping("LeftMouse",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, "LeftMouse");

        inputManager.addMapping("RightMouse", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "RightMouse");

        inputManager.addMapping("MouseWheelForward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(analogListener, "MouseWheelForward");

        inputManager.addMapping("MouseWheelBackward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(analogListener, "MouseWheelBackward");
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

        if(myClient != null)
        {
            myClient.close();
        }
        
        super.destroy();
        executor.shutdown();
    }

    private void connectToServer() {

        try {
            myClient = Network.connectToServer(serverConection.getAdress(), serverConection.getIpPort(), serverConection.getUdpPort());

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        addListener();

        if (myClient != null) {
            myClient.start();

        }
    }



    private void addListener() {
        myClient.addMessageListener(new ClientListener(), PingMessage.class);
        myClient.addMessageListener(new ClientListener(), createEntityMessage.class);
        myClient.addMessageListener(new ClientListener(), PositionMessage.class);
    }
    /**
     * Use ActionListener to respond to pressed/released inputs (key presses,
     * mouse clicks)
     */
    private final com.jme3.input.controls.ActionListener actionListener = new com.jme3.input.controls.ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {

            if(myClient.isConnected())
            {
            
            for (Entity entity : entities) {
                // entity.couldMove = true;

                if ("LeftMouse".equals(name) && pressed) {
                    entity.sendNewPositionMessage = true;
                    entity.nextPosition = entity.getEntity().getLocalTranslation().addLocal(1f, 0, 0);
                    if (myClient != null) {
                        System.out.println(name + " = " + pressed);
                        PositionMessage message = new PositionMessage("" + entity.getID(), (entity.nextPosition).toString());
                        myClient.send(message);
                        System.out.println("send new Position Message" + message.toString());
                    }
                }
                if ("RightMouse".equals(name) && pressed) {
                    entity.sendNewPositionMessage = true;
                    entity.nextPosition = entity.getEntity().getLocalTranslation().addLocal(-1f, 0, 0);
                    System.out.println(name + " = " + pressed);

                    if (myClient != null) {
                        PositionMessage message = new PositionMessage("" + entity.getID(), (entity.nextPosition).toString());
                        myClient.send(message);
                        System.out.println("send new Position Message" + message.toString());
                    }
                }
            }

        }
        }
    };
    /**
     * Use AnalogListener to respond to continuous inputs (key presses, mouse
     * clicks)
     */
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            System.out.println(name + " = " + value);
        }
    };

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
                        entity.recevedNewPositionMessage = true;
                        entity.setNextPosition(positionMessage.position);
                        System.out.println("reseved new Position" + positionMessage.position);
                    }
                }

            }

        }
    }

}
