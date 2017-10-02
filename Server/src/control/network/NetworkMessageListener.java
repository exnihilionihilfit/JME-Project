/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.network.HostedConnection;
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
    static final Queue<NetworkMessages.RegisterOnServerMessage> REGISTER_ON_SERVER_MESSAGE = new LinkedList<>();

    public Queue<NetworkMessages.PingMessage> getPingMessages() {
        return PING_MESSAGES;
    }

    public Queue<NetworkMessages.CreateEntityMessage> getCreateEntityMessage() {
        return CREATE_ENTITY_MESSAGES;
    }

    public Queue<NetworkMessages.EntityPositionMessage> getEntityPositionMessage() {
        return ENTITY_POSITION_MESSAGE;
    }

    public class ServerMoveOrderListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof NetworkMessages.EntityPositionMessage) {

                // NetworkMessageHandling.handleEntityPositionMessage(source, message);
                ENTITY_POSITION_MESSAGE.add((NetworkMessages.EntityPositionMessage) message);
            }

        }
    }

    public class ServerListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(HostedConnection source, Message message) {

            if (message instanceof NetworkMessages.RegisterOnServerMessage) {
                // should used only once at registration
                NetworkMessages.RegisterOnServerMessage registerOnServerMessage = (NetworkMessages.RegisterOnServerMessage) message;

                NetworkMessageHandling.addPlayer(source, registerOnServerMessage);

               

            }
            if (message instanceof NetworkMessages.PingMessage) {
                // do something with the message
                NetworkMessages.PingMessage pingMessage = (NetworkMessages.PingMessage) message;
                System.out.println("Server received '" + pingMessage.hello + "' from client #" + source.getId());
            }

            if (message instanceof NetworkMessages.CreateEntityMessage) {
                // NetworkMessageHandling.handleCreateEntityMessage(source, message);
                CREATE_ENTITY_MESSAGES.add((NetworkMessages.CreateEntityMessage) message);
            }

        }
    }

}
