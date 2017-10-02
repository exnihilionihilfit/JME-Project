/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import control.CollisionTypes;
import control.Order;
import control.OrderTypes;

@Serializable
public class EntityContainer {

    public Vector3f position = new Vector3f(1, 0, 0);
    public EntityTypes type = EntityTypes.NOT_DEFINED;
    public int entityId;
    public long playerId = 0;
    public Vector3f destination = new Vector3f(0, 0, 0);
    public float speed = 15.0f;
    public Vector3f direction = new Vector3f(1, 0, 0);
    public boolean isNewCreated = true;
    public boolean collided = false;
    public Vector3f lastPosition = new Vector3f();
    public String name;
    public float size = 10;
    public boolean hasStaticPosition = false;
    public CollisionTypes collisionTypes = CollisionTypes.MOVEABLE;
    public boolean isBuildable = false;
    public boolean isMoveable = true;
    public Order activeOrder = new Order(playerId, entityId, position, OrderTypes.STAY); 
     public boolean isMoving = false;
     
    public EntityContainer() {

    }

    public EntityContainer(long playerId, int entityId, String name, EntityTypes type, Vector3f position) {
        this.playerId = playerId;
        this.entityId = entityId;
        this.position = position;
        this.type = type;
        this.name = name;
        this.activeOrder = new Order(playerId, entityId, position, OrderTypes.STAY);
    }

}
