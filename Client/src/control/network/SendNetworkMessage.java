/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import main.Main;
import model.Entity;

/**
 *
 * @author novo
 */
public class SendMessage {
    
    public void sendEntityPositionMessage(Entity entity)
    {
        /*
       entity.sendNewPositionMessage(true);
                    entity.setNextPosition( entity.getEntity().getLocalTranslation().addLocal(1f, 0, 0));
                    if (myClient != null) {
                    
                        NetworkMessages.EntityPositionMessage message = new NetworkMessages.EntityPositionMessage("" + entity.getID(), (entity.getNextPosition()).toString());
                        myClient.send(message);
                        System.out.println("send new Position Message" + message.toString());
                    }
                      entity.setSendNewPositionMessage(true);
                    entity.setNextPosition(entity.getEntity().getLocalTranslation().addLocal(-1f, 0, 0));
                    System.out.println(name + " = " + pressed);

                    if (myClient != null) {
                        Main.PositionMessage message = new Main.PositionMessage("" + entity.getID(), (entity.getNextPositionAsString()));
                        myClient.send(message);
                        System.out.println("send new Position Message" + message.toString());
                    }  */
    }
   
               
    
}
