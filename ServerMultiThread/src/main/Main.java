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

        System.out.println("start server ... ");
        System.out.println(" register network messages ");

        try {
            myServer = Network.createServer(6143, 6142);

            Serializer.registerClass(NetworkMessages.PingMessage.class);
            Serializer.registerClass(NetworkMessages.EntityPositionMessage.class);
            Serializer.registerClass(NetworkMessages.CreateEntityMessage.class);
            Serializer.registerClass(NetworkMessages.RegisterOnServer.class);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(" add network message listener");
        NetworkMessageListener networkMessageListener = new NetworkMessageListener();

      //  myServer.addMessageListener(networkMessageListener.new ServerMessageListener(), ... );
        myServer.addMessageListener(networkMessageListener.new ServerListener(), 
                NetworkMessages.CreateEntityMessage.class, 
                NetworkMessages.EntityPositionMessage.class,
                 NetworkMessages.PingMessage.class,
                 NetworkMessages.RegisterOnServer.class);

        
        if (myServer != null) {
            myServer.start();
        }

        if(myServer.isRunning())
        {
            System.out.println("server started ");
        }
        

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

            //NetworkMessages.PingMessage message = new NetworkMessages.PingMessage(" ", 0L);
            //myServer.broadcast(message);

            for (HostedConnection connection : myServer.getConnections()) {
               NetworkMessages.PingMessage message = new NetworkMessages.PingMessage(" ... server message ", 0L);
                connection.send(message);
            }
        }

    }

}
