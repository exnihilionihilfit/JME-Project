package main;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import control.EntityAction;
import control.network.NetworkMessageHandling;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;
import control.SimpleCollision;
import java.util.ArrayList;
import model.Entities;
import model.EntityContainer;
import model.Players;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    Server server = null;
    long lastTime = 0;
    long timeInterval = 50;
    int PORT_IP = 6143;
    int PORT_UDP = 6142;
    private static Players players;
    ArrayList<EntityContainer> filteredEntityContainers = new ArrayList<>();

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
            Serializer.registerClass(NetworkMessages.RegisterOnServer.class);
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
                NetworkMessages.RegisterOnServer.class,
                NetworkMessages.EntitiesListMessage.class,
                EntityContainer.class);

        server.addMessageListener(networkMessageListener.new ServerMoveOrderListener(),
                NetworkMessages.EntityPositionMessage.class);

        if (server != null) {
            server.start();
        }

        if (server.isRunning()) {
            System.out.println("server started ");
        }

        players = new Players();

    }

    @Override
    public void simpleUpdate(float tpf) {

        if ((lastTime + timeInterval) < System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();

            /**
             * move each entity and only the entities which are moved will be
             * send to reduce traffic. After all any change should set a flag to
             * send it for now only movement will do so Also all other
             * playerId's should set to -1 so nobody could mess with all id's
             * around ;) PROBLEM: java.util.ConcurrentModificationException ^^
             * should be realy multy-threaded ;) AND: A reconnected player would
             * get the whole list of entities !
             */
            NetworkMessages.EntitiesListMessage entitiesListMessage = new NetworkMessages.EntitiesListMessage(Entities.entityContainers);

            for (Player player : Players.getPlayerList()) {
                if (player.getConnection() != null) {

                    player.getConnection().send(entitiesListMessage);

                }

            }
        } else {
            filteredEntityContainers.clear();

            for (EntityContainer entityContainer : Entities.entityContainers) {
                
                EntityAction.moveEntityToPosition(entityContainer);

                if (entityContainer.moveToPositon || entityContainer.isNewCreated) {
                    filteredEntityContainers.add(entityContainer);
                    entityContainer.isNewCreated = false;

                }
            }

            NetworkMessageHandling.handleCreateEntityMessage();
            NetworkMessageHandling.handleEntityPositionMessage();
            
            SimpleCollision.checkCollision(Entities.entityContainers);
        }

    }

}
