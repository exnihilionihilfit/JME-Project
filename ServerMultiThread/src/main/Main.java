package main;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import control.EntityAction;
import control.Map;
import control.network.NetworkMessageHandling;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;
import control.SimpleCollision;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import model.Entities;
import model.EntityContainer;
import model.Players;
import control.PropertiesHandler;
import control.SendMessageToClient;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    Server server = null;
    long lastTime = 0;
    long timeInterval = 40;
    int PORT_IP = 6143;
    int PORT_UDP = 6142;
    int maxMessageSize = 50;
    private static Players players;


    public static void main(String[] args) {

        Main app = new Main();
        app.start(JmeContext.Type.Headless); // headless type for servers!
    }

    @Override
    public void simpleInitApp() {

        System.out.println("start server ... ");
        System.out.println(" register network messages ");

        try {
            server = Network.createServer(PORT_IP, PORT_UDP);

            Serializer.registerClass(NetworkMessages.PingMessage.class);
            Serializer.registerClass(NetworkMessages.EntityPositionMessage.class);
            Serializer.registerClass(NetworkMessages.CreateEntityMessage.class);
            Serializer.registerClass(NetworkMessages.RegisterOnServerMessage.class);
            Serializer.registerClass(NetworkMessages.EntitiesListMessage.class);
            Serializer.registerClass(EntityContainer.class);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(" add network message listener");
        NetworkMessageListener networkMessageListener = new NetworkMessageListener();

        //  myServer.addMessageListener(networkMessageListener.new ServerMessageListener(), ... );
        server.addMessageListener(networkMessageListener.new ServerListener(),
                NetworkMessages.CreateEntityMessage.class,
                NetworkMessages.EntityPositionMessage.class,
                NetworkMessages.PingMessage.class,
                NetworkMessages.RegisterOnServerMessage.class,
                NetworkMessages.EntitiesListMessage.class,
                EntityContainer.class);

        server.addMessageListener(networkMessageListener.new ServerMoveOrderListener(),
                NetworkMessages.EntityPositionMessage.class);
        
        // load all entity properties
        PropertiesHandler.load();

        if (server != null) {
            server.start();
        }

        if (server.isRunning()) {
            System.out.println("server started ");
        }

        players = new Players();
        Map map = new Map(1000, 300);

    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();

            NetworkMessageHandling.handleCreateEntityMessage();
            NetworkMessageHandling.handleEntityPositionMessage();

            Thread sendClientMessages = new Thread(new SendMessageToClient(true, null));
            
            sendClientMessages.start();

        }

    }

}
