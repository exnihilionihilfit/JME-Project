/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import control.GameOptions;
import control.network.NetworkMessages.EntityPositionMessage;
import java.util.List;
import java.util.UUID;
import model.Client;

/**
 *
 * @author novo
 */
public class NetworkMessageListener {
    List<HostedConnection> connectedClients;
        List<Client> clientList;
    
    
     public class ServerMessageListener implements MessageListener<HostedConnection> {

    

        @Override
        public void messageReceived(HostedConnection source, Message message) {
           // else....

        }

     
    }

    public class ServerListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {
            
            if(message instanceof NetworkMessages.RegisterOnServer)
            {
                // should used only once at registration
                NetworkMessages.RegisterOnServer registerOnServerMessage = (NetworkMessages.RegisterOnServer) message;
               
                UUID uuid = UUID.randomUUID();
                registerOnServerMessage.clientID = uuid.getLeastSignificantBits();
                source.send(registerOnServerMessage);
                
                System.out.println(" register client name: "+registerOnServerMessage.clienUserName+" id: "+registerOnServerMessage.clientID);
            }
              if (message instanceof NetworkMessages.PingMessage) {
                // do something with the message
                NetworkMessages.PingMessage pingMessage = (NetworkMessages.PingMessage) message;
                System.out.println("Server received '" + pingMessage.hello + "' from client #" + source.getId());
            
               
                if( !connectedClients.contains(source) )
                {
                    connectedClients.add(source);
                    createClientContainer(source);
                }
            }

            if (message instanceof NetworkMessages.EntityPositionMessage) {
                NetworkMessages.EntityPositionMessage positionMessage = (NetworkMessages.EntityPositionMessage) message;

                // just send the position back without check ... for now ...
                String newPositonVector = positionMessage.position;
                String entityID = positionMessage.entityID;

                System.out.println("Client [" + source.getId() + "] Validate position: " + newPositonVector);

                EntityPositionMessage newPositionMessage = new EntityPositionMessage(entityID, newPositonVector);
                source.send(newPositionMessage);
            }
        }
    }
    
           private void createClientContainer(HostedConnection source) 
       {
           Client newClient = new Client(GameOptions.clientStartCredits);
           newClient.addHostedConnection(source);
           this.clientList.add(newClient);
        }
    
}
