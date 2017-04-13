/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author novo
 */
public class NetworkMessages {
    
    
    @Serializable
    public static class PingMessage extends AbstractMessage {

         String hello;       // custom message data
         long time;

        public PingMessage() {
        }    // empty constructor

        public PingMessage(String s, long t) {
            hello = s;
            time = t;
        } // custom constructor
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
    public static class createEntityMessage extends AbstractMessage {

         boolean isNewEntity = false;

        public createEntityMessage() {

        }

        public createEntityMessage(boolean isNewEntity) {
            this.isNewEntity = isNewEntity;
        }
    }


}
