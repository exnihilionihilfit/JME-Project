/**
 * This are the message classes to send data to server and back
 *
 * IMPORTANT: The class structor and its location must be the same on client
 * and server side!
 */
package control.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.UUID;

/**
 *
 * @author novo
 */
public class NetworkMessages {

    @Serializable
    public static class RegisterOnServer extends AbstractMessage {

        String clienUserName;
        long clientID;

        public RegisterOnServer() {
            // empty constructor
        }

        public RegisterOnServer(String userName) {
            this.clienUserName = userName;
        }

        public RegisterOnServer(String clientUserName, long uuid) {
            this.clienUserName = clientUserName;
            this.clientID = uuid;
        }
    }

    @Serializable
    public static class PingMessage extends AbstractMessage {

        String hello;       // custom message data
        long time;

        public PingMessage() {
            // empty constructor
        }

        public PingMessage(String s, long t) {
            hello = s;
            time = t;
        }
    }

    @Serializable
    public static class EntityPositionMessage extends AbstractMessage {

        String position;
        String entityID;

        public EntityPositionMessage() {

        }

        public EntityPositionMessage(String position, String entityID) {
            this.position = position;
            this.entityID = entityID;
        }
    }

    @Serializable
    public static class CreateEntityMessage extends AbstractMessage {

        boolean isNewEntity = false;

        public CreateEntityMessage() {

        }

        public CreateEntityMessage(boolean isNewEntity) {
            this.isNewEntity = isNewEntity;
        }
    }

}
