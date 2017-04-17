/**
 * Is used to transfer the needed data (only data) from server to client
 * entities could only createt on server side after the client sends a create
 * message and the server confirms that it could be done 
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
