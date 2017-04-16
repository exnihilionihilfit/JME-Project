/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

    @Serializable
    public class EntityContainer {
    
    
    public Vector3f position;
    public String type = "battleship";
    public int entityId;
    public long playerId = 0;
    
    public EntityContainer()
    {
        
    }
    
    public EntityContainer(long playerId, int entityId, String type, Vector3f position)
    {
        this.playerId = playerId;
        this.entityId = entityId;
        this.position = position;
        this.type = type;
    }
    
  
    
}
