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

/**
 *
 * @author novo
 */
public class NetworkMessageListener {

    static final Queue<NetworkMessages.PingMessage> PING_MESSAGES = new LinkedList<>();

    static final Queue<NetworkMessages.CreateEntityMessage> CREATE_ENTITY_MESSAGES = new LinkedList<>();
    static final Queue<NetworkMessages.EntityPositionMessage> ENTITY_POSITION_MESSAGE = new LinkedList<>();

 
    
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

    public class ClientListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message message) {
            if (message instanceof NetworkMessages.PingMessage) {
                // do something with the message
                NetworkMessages.PingMessage helloMessage = (NetworkMessages.PingMessage) message;
                PING_MESSAGES.add(helloMessage);
                //  System.out.println("Client #" + source.getId() + " received: '" + helloMessage.hello + "'");
            }

            if (message instanceof NetworkMessages.CreateEntityMessage) {
                NetworkMessages.CreateEntityMessage createEntityMessage = (NetworkMessages.CreateEntityMessage) message;
                CREATE_ENTITY_MESSAGES.add(createEntityMessage);
                //System.out.println(createEntityMessage.isNewEntity);
            }

            if (message instanceof NetworkMessages.EntityPositionMessage) {
                NetworkMessages.EntityPositionMessage positionMessage = (NetworkMessages.EntityPositionMessage) message;
                ENTITY_POSITION_MESSAGE.add(positionMessage);
                //System.out.println("--> NewPositionReseaved "+positionMessage.entityID);
            }

        }
    }

}
