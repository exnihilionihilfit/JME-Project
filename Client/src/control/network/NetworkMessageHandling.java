/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import control.Helper;
import main.Main;
import model.Entity;
import model.EntityContainer;

/**
 *
 * @author novo
 */
public class NetworkMessageHandling {

    /*
    take each call one message and handle it
    returns false if the deque is empty and no messages are there
    returns true if a massage is handeld
     */
    public static boolean handlePingMessage() {
        NetworkMessages.PingMessage pingMessage = NetworkMessageListener.PING_MESSAGES.poll();

        if (pingMessage != null) {
            System.out.println(pingMessage.hello);

            return true;
        } else {
            return false;
        }
    }

    /*
    take each call one message and handle it
    returns false if the deque is empty and no messages are there
    returns true if a massage is handeld
     */
    public static boolean handleEntityPositionMessage() {
        NetworkMessages.EntityPositionMessage entityPositionMessage = NetworkMessageListener.ENTITY_POSITION_MESSAGE.poll();

        if (entityPositionMessage != null) {

            for (Entity entity : Main.getEntities()) {
                if (entityPositionMessage.entityID == entity.getID()) {
                    entity.setRecevedNewPositionMessage(true);
                    entity.setNextPosition(Helper.convertStringToVector3f(entityPositionMessage.position));

                }
            }

            return true;
        } else {
            return false;
        }

    }

    public static void handleEntitiesListMessage(Main main) {
        NetworkMessages.EntitiesListMessage entityPositionMessage = NetworkMessageListener.ENTITY_LIST_MESSAGE.poll();

        if (entityPositionMessage != null) {
            main.setEntities(entityPositionMessage.entities);
        }

    }

}
