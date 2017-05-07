/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import control.CollisionTypes;

@Serializable
public class EntityContainer {

    public Vector3f position = new Vector3f(1, 0, 0);
    public String type = "entity";
    public int entityId;
    public long playerId = 0;
    public Vector3f destination = new Vector3f(0, 0, 0);
    
    public float speed = 15.0f;
    public long lastMoveUpdate;
    public boolean moveToPositon;
    public Vector3f direction = new Vector3f(1, 0, 0);
    public boolean isNewCreated = true;
    public boolean collided = false;
    public Vector3f lastPosition = new Vector3f();
    public String name;
    public float size = 10;
    public boolean hasStaticPosition = false;
    public CollisionTypes collisionTypes = CollisionTypes.MOVEABLE;

    public EntityContainer() {

    }

    public EntityContainer(long playerId, int entityId, String name, String type, Vector3f position) {
        this.playerId = playerId;
        this.entityId = entityId;
        this.position = position;
        this.type = type;
        this.name = name;

    }

}
