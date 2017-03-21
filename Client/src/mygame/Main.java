package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.AbstractMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
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

    Client myClient = null;
    long lastTime = System.currentTimeMillis();
    long timeInterval = 50000;

    public static void main(String[] args) {
        Main app = new Main();
        app.start(JmeContext.Type.Display); // standard display type
    }

    @Override
    public void simpleInitApp() {
        Serializer.registerClass(HelloMessage.class);
        System.out.println("connecting ....");

        connectToServer();

        if (myClient != null) {
            while (myClient.isConnected() == false) {
                System.out.println("can't connect to server... retry");
                connectToServer();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (myClient != null) {
            System.out.println(myClient.isConnected());

            myClient.addMessageListener(new ClientListener(), HelloMessage.class);

            Message message = new HelloMessage("Hello World!");
            myClient.send(message);
        }

    }

    private void connectToServer() {

        try {
            myClient = Network.connectToServer("localhost", 6143);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (myClient != null) {
            myClient.start();

        }
    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();

            if (myClient.isConnected() && myClient.isStarted()) {

                myClient.close();
                System.out.println("connected ... " + myClient.isConnected());
            }
        }

    }

    @Serializable
    public static class HelloMessage extends AbstractMessage {

        private String hello;       // custom message data

        public HelloMessage() {
        }    // empty constructor

        public HelloMessage(String s) {
            hello = s;
        } // custom constructor
    }

    public  class ClientListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message m) {
            if (m instanceof HelloMessage)             
            {
                // do something with the message
                HelloMessage helloMessage = (HelloMessage) m;
                System.out.println("Client #" + source.getId() + " received: '" + helloMessage.hello + "'");
            }
           
        }
    }

}
