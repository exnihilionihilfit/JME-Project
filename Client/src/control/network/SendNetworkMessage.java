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
      
    }
    
    public void sendPingMessage(String message)
    {
        long time = System.currentTimeMillis();
        NetworkMessages.PingMessage pingMessage = new NetworkMessages.PingMessage(message,time);
        client.send(pingMessage);
    }
    
    public void sendCreateEntityMessage()
    {
        NetworkMessages.CreateEntityMessage createEntityMessage = new NetworkMessages.CreateEntityMessage();
        client.send(createEntityMessage);
    }
   
               
    
}
