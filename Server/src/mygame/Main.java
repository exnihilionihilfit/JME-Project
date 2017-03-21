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

        Serializer.registerClass(HelloMessage.class);
        System.out.println("start server...");
        try {
            myServer = Network.createServer(6143);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (myServer != null) {
            myServer.start();
        }

        System.out.println(myServer.isRunning());

        myServer.addMessageListener(new ServerListener(), HelloMessage.class);

    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();
            System.out.println(myServer.getConnections().size());
            
            Message message = new HelloMessage("to all clients");
            myServer.broadcast(message);
        }

    }

    @Serializable
    public static class HelloMessage extends AbstractMessage {

        private String hello; // custom message data

        public HelloMessage() {
        }    // empty constructor

        public HelloMessage(String s) {
            hello = s;
        } // custom constructor
    }

    public class ServerListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof HelloMessage)
            {
                // do something with the message
                HelloMessage helloMessage = (HelloMessage) message;
                System.out.println("Server received '" + helloMessage.hello + "' from client #" + source.getId());
            } // else....
        }
    }
}
