/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.LinkedList;
import java.util.Queue;
import model.Player;

/**
 *
 * @author novo
 */
public class NetworkMessageListener {

    static final Queue<NetworkMessages.PingMessage> PING_MESSAGES = new LinkedList<>();
    static final Queue<NetworkMessages.CreateEntityMessage> CREATE_ENTITY_MESSAGES = new LinkedList<>();
    static final Queue<NetworkMessages.EntityPositionMessage> ENTITY_POSITION_MESSAGE = new LinkedList<>();
    static final Queue<NetworkMessages.EntitiesListMessage> ENTITY_LIST_MESSAGE = new LinkedList<>();

      
    public Queue<NetworkMessages.PingMessage> getPingMessages()
    {
        return PING_MESSAGES;
    }
    
    public Queue<NetworkMessages.CreateEntityMessage>  getCreateEntityMessage()
    {
        return CREATE_ENTITY_MESSAGES;
    }
    
    public Queue<NetworkMessages.EntityPositionMessage> getEntityPositionMessage()
    {
        return ENTITY_POSITION_MESSAGE;
    }
    
     public Queue<NetworkMessages.EntitiesListMessage> getEntityListMessage()
    {
        return ENTITY_LIST_MESSAGE;
    }
     public class ClientMoveOrderListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message message) {
                        
                  if (message instanceof NetworkMessages.EntityPositionMessage) {
                NetworkMessages.EntityPositionMessage positionMessage = (NetworkMessages.EntityPositionMessage) message;
                ENTITY_POSITION_MESSAGE.add(positionMessage);
                System.out.println("--> NewPositionReseaved "+positionMessage.position);
        }
         
     }

            }

    public class ClientListener implements MessageListener<Client> {

       

        @Override
        public void messageReceived(Client source, Message message) {
            
             // should used only once at registration
            if(message instanceof NetworkMessages.RegisterOnServer)
            {
               
                NetworkMessages.RegisterOnServer registerOnServerMessage = (NetworkMessages.RegisterOnServer) message;
               
                // 0L is default value and idicates no registration yet
                if(Player.getPlayerId() == 0L)
                {
                    Player.setPlayerId(registerOnServerMessage.playerId);
                     System.out.println(" client is registerd with name: "+registerOnServerMessage.clienUserName+" id: "+registerOnServerMessage.playerId);
                }
                
            }
            
            if (message instanceof NetworkMessages.PingMessage) {
                // do something with the message
                NetworkMessages.PingMessage pingMessage = (NetworkMessages.PingMessage) message;
                PING_MESSAGES.add(pingMessage);
                  System.out.println("Client #" + source.getId() + " received: '" + pingMessage.hello + "'");
            }

            if (message instanceof NetworkMessages.CreateEntityMessage) {
                NetworkMessages.CreateEntityMessage createEntityMessage = (NetworkMessages.CreateEntityMessage) message;
                CREATE_ENTITY_MESSAGES.add(createEntityMessage);
                System.out.println(" reseaved create of entity successfull "+createEntityMessage.isNewEntity);
            }

       
            
           if (message instanceof NetworkMessages.EntitiesListMessage) {
                NetworkMessages.EntitiesListMessage entitiesListMessage = ( NetworkMessages.EntitiesListMessage ) message;
                ENTITY_LIST_MESSAGE.add(entitiesListMessage);
            
           }
            
            
            

        }
    }

}
