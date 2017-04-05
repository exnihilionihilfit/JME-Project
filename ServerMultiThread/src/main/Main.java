package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.AbstractMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    Server myServer = null;
    long lastTime = 0;
    long timeInterval = 5000;

    public static void main(String[] args) {

        Main app = new Main();
        app.start(JmeContext.Type.Headless); // headless type for servers!
    }

    @Override
    public void simpleInitApp() {

        System.out.println("start server...");

        try {
            myServer = Network.createServer(6143, 6142);
            Serializer.registerClass(PingMessage.class);
            Serializer.registerClass(PositionMessage.class);
            Serializer.registerClass(createEntityMessage.class);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        myServer.addMessageListener(new ServerMessageListener(), PingMessage.class);
        myServer.addMessageListener(new ServerListener(), createEntityMessage.class);
        myServer.addMessageListener(new ServerListener(), PositionMessage.class);

        if (myServer != null) {
            myServer.start();
        }

        System.out.println(myServer.isRunning());

    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();

            Message message = new PingMessage("to all clients", 0L);
            myServer.broadcast(message);

            for (HostedConnection connection : myServer.getConnections()) {
                message = new PingMessage("to you", 0L);
                connection.send(message);
            }
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

    public class ServerMessageListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof PingMessage) {
                // do something with the message
                PingMessage pingMessage = (PingMessage) message;
                System.out.println("Server received '" + pingMessage.hello + "' from client #" + source.getId());
            } // else....

        }
    }

    public class ServerListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {

            if (message instanceof PositionMessage) {
                PositionMessage positionMessage = (PositionMessage) message;

                // just send the position back without check ... for now ...
                String newPositonVector = positionMessage.position;
                String entityID = positionMessage.entityID;

                System.out.println("Client [" + source.getId() + "] Validate position: " + newPositonVector);

                PositionMessage newPositionMessage = new PositionMessage(entityID, newPositonVector);
                source.send(newPositionMessage);
            }
        }
    }
}
