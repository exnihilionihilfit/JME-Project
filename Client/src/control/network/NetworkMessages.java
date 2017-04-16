/**
 * This are the message classes to send data to server and back
 *
 * IMPORTANT: The class structor and its location must be the same on client
 * and server side!
 */
package control.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import model.EntityContainer;

/**
 *
 * @author novo
 */
public class NetworkMessages {

    @Serializable
    public static class RegisterOnServer extends AbstractMessage {

        String clienUserName;
        long playerId;

        public RegisterOnServer() {
            // empty constructor
        }

        public RegisterOnServer(String userName) {
            this.clienUserName = userName;
        }

        public RegisterOnServer(String clientUserName, long uuid) {
            this.clienUserName = clientUserName;
            this.playerId = uuid;
        }
    }

    @Serializable
    public static class PingMessage extends AbstractMessage {

        String hello;       // custom message data
        long time;
        long playerId;

        public PingMessage() {
            // empty constructor
        }

        public PingMessage(long playerId,String s, long t) {
            this.playerId = playerId;
            this.hello = s;
            this.time = t;
        }
    }

    @Serializable
    public static class EntityPositionMessage extends AbstractMessage {

        String position;
        int entityID;
        long payerId;
        
        public EntityPositionMessage() {

        }

        public EntityPositionMessage(long playerId, int entityID, String position) {
            this.payerId = playerId;
            this.position = position;
            this.entityID = entityID;
        }
    }

    @Serializable
    public static class CreateEntityMessage extends AbstractMessage {

        boolean isNewEntity = false;
        long playerId;

        public CreateEntityMessage() {

        }

        public CreateEntityMessage(long playerId,boolean isNewEntity) {
            this.playerId = playerId;
            this.isNewEntity = isNewEntity;
        }
    }
    
    @Serializable
    public static class EntitiesListMessage extends AbstractMessage{
        
        //contains shipId, type, position
       ArrayList<EntityContainer> entities;
        
        public EntitiesListMessage()
        {
            
        }
        public EntitiesListMessage(ArrayList<EntityContainer> entities)
        {
            this.entities = entities;
        }
        
    }

}
