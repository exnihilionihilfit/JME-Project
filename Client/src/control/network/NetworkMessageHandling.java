/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import java.util.List;
import main.Main;
import model.Entity;

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
            
          for(Entity entity:Main.getEntities())
          {
              if(Integer.parseInt(entityPositionMessage.entityID) == entity.getID())
              {
                        entity.setRecevedNewPositionMessage(true);
                        entity.setNextPosition(entityPositionMessage.position);
                        System.out.println("set new position"+entityPositionMessage.position);
              }
          }

            return true;
        } else {
            return false;
        }

    }

}
