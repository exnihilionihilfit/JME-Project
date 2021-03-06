/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import main.Main;

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

    public static void handleEntitiesListMessage(Main main) {
        NetworkMessages.EntitiesListMessage entityPositionMessage = NetworkMessageListener.ENTITY_LIST_MESSAGE.poll();

        if (entityPositionMessage != null) {
            main.setEntitiesPositionFromServerList(entityPositionMessage.entities);
        }

    }

}
