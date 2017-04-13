package main;

import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;


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
    List<HostedConnection> connectedClients;
    List<Client> clientList;

    public static void main(String[] args) {

        Main app = new Main();
        app.start(JmeContext.Type.Headless); // headless type for servers!
    }

    @Override
    public void simpleInitApp() {

        System.out.println("start server...");

        try {
            myServer = Network.createServer(6143, 6142);

            Serializer.registerClass(NetworkMessages.PingMessage.class);
            Serializer.registerClass(NetworkMessages.EntityPositionMessage.class);
            Serializer.registerClass(NetworkMessages.createEntityMessage.class);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        NetworkMessageListener networkMessageListener = new NetworkMessageListener();

        myServer.addMessageListener(networkMessageListener.new ServerMessageListener(), NetworkMessages.PingMessage.class);
        myServer.addMessageListener(networkMessageListener.new ServerListener(), NetworkMessages.createEntityMessage.class);
        myServer.addMessageListener(networkMessageListener.new ServerListener(), NetworkMessages.EntityPositionMessage.class);

        if (myServer != null) {
            myServer.start();
        }

        System.out.println(myServer.isRunning());

        /*
        Init Game-Elements
         */
        this.connectedClients = new ArrayList<>();
        this.clientList = new ArrayList<>();

    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();

            NetworkMessages.PingMessage message = new NetworkMessages.PingMessage("to all clients", 0L);
            myServer.broadcast(message);

            for (HostedConnection connection : myServer.getConnections()) {
                message = new NetworkMessages.PingMessage("to you", 0L);
                connection.send(message);
            }
        }

    }

}
