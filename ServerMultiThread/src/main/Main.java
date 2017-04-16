package main;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import control.network.NetworkMessageListener;
import control.network.NetworkMessages;
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
    long timeInterval = 5000;
    int PORT_IP = 6143;
    int PORT_UDP = 6142;
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

            //NetworkMessages.PingMessage message = new NetworkMessages.PingMessage(" ", 0L);
            //myServer.broadcast(message);
            /*
            for (HostedConnection connection : myServer.getConnections()) {
               NetworkMessages.PingMessage message = new NetworkMessages.PingMessage(" ... server message ", 0L);
                connection.send(message);
            }
             */
            
           // String[] entitesAsString = Helper.prepareEntitiesList(Players.getPlayerList());
            
            NetworkMessages.EntitiesListMessage entitiesListMessage = new NetworkMessages.EntitiesListMessage(Entities.all);
           
            
            for(Player player:Players.getPlayerList())
            {
                if(player.getConnection() != null)
                {
                      player.getConnection().send(entitiesListMessage);
                    
                }
              
            }
        }

    }

}
