package main;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import control.Map;
import control.Order;
import control.network.NetworkMessageHandling;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;
import model.EntityContainer;
import model.Players;
import control.PropertiesHandler;
import control.entity.HandleEntityCondition;
import control.pathfinding.Pathfinding;
import control.server.Actions;
import java.awt.Rectangle;

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

    private Actions serverActionsControl;
    private HandleEntityCondition entityHandling;
    private Thread entityHandlingThread;
    private Thread serverActionControlThread;

    public static void main(String[] args) {

        Main app = new Main();
        app.start(JmeContext.Type.Headless); // headless type for servers!
    }
    private Pathfinding path;

    @Override
    public void simpleInitApp() {

        setShowSettings(false); 
        
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
            Serializer.registerClass(Order.class);

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
                EntityContainer.class,
                Order.class);

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

        entityHandling = new HandleEntityCondition();
        entityHandlingThread = new Thread(entityHandling);
        entityHandlingThread.start();

        serverActionsControl = new Actions();
        serverActionControlThread = new Thread(serverActionsControl);
        serverActionControlThread.start();

    
    }

    @Override
    public void simpleUpdate(float tpf) {

        if (serverActionsControl.isPaused()) {
            // pausing

        } else if (serverActionsControl.isRunning()) {

            if ((lastTime + timeInterval) < System.currentTimeMillis()) {
                lastTime = System.currentTimeMillis();

                NetworkMessageHandling.handleCreateEntityMessage();
                NetworkMessageHandling.handleEntityPositionMessage();

            }
        } else if (serverActionsControl.isShutdown()) {
            
            Players.getPlayerList().forEach((player) -> {
                player.getConnection().close("server shutdown");
            });

            try {
                entityHandling.stop();

                while (entityHandlingThread.isAlive()) {
                    System.out.print(".");
                    Thread.sleep(1000);
                }

                serverActionsControl.stop();

                while (serverActionControlThread.isAlive()) {
                    System.out.print(".");
                    Thread.sleep(1000);
                }

                if (server.isRunning()) {
                    server.close();
                }

            } catch (InterruptedException e) {
                System.out.print("error during shutdown");
            }

            System.exit(0);

        }
    }

}
