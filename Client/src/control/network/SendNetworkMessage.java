/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.network;

import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import model.Entity;

/**
 *
 * @author novo
 */
public class SendNetworkMessage {
    
    Client client;
    
    public SendNetworkMessage(Client client)
    {
        this.client = client;
    }
    
    public void sendEntityPositionMessage(Entity entity, Vector3f newPosition)
    {
        
         NetworkMessages.EntityPositionMessage message = new NetworkMessages.EntityPositionMessage("" + entity.getID(), newPosition.toString());
         client.send(message);
         //System.out.println("--> send EntityPositionMessage" + message.toString());
        
       /*
         entity.sendNewPositionMessage(true);
                    entity.setNextPosition( entity.getEntity().getLocalTranslation().addLocal(1f, 0, 0));
                    if (client != null) {
                    
                       
                    }
                      entity.setSendNewPositionMessage(true);
                    entity.setNextPosition(entity.getEntity().getLocalTranslation().addLocal(-1f, 0, 0));
         

                    if (client != null) {
                        NetworkMessages.EntityPositionMessage message = new NetworkMessages.EntityPositionMessage("" + entity.getID(), (entity.getNextPositionAsString()));
                        client.send(message);
                        System.out.println("send new Position Message" + message.toString());
                    }  
         */
    }
   
               
    
}
