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
import java.util.ArrayList;

@Serializable
public class EntityContainer {

    private Vector3f position = new Vector3f(10, 0, 10);
    private EntityTypes type = EntityTypes.NOT_DEFINED;
    private int entityId;
    private long playerId = 0;

    private float speed = 15.0f;
    private Vector3f direction = new Vector3f(1, 0, 0);
    private boolean isNewCreated = true;
    private boolean collided = false;
    private Vector3f lastPosition = new Vector3f();
    private String name;
    private float size = 10;
    private boolean hasStaticPosition = false;
    private CollisionTypes collisionTypes = CollisionTypes.MOVEABLE;
    private boolean isBuildable = false;
    private boolean isMoveable = true;
    private Order activeOrder = new Order(playerId, entityId, position, OrderTypes.STAY);
    private boolean isMoving = false;
    private boolean isChanged = false;

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
    
    public void confirmeChanges()
    {
        this.isChanged = false;
    }
    
    public boolean isChanged(){
        return this.isChanged;
    }

    public void setPosition(Vector3f position) {
        this.isChanged = true;
        this.position = position;
    }

    public void setType(EntityTypes type) {
        this.isChanged = true;
        this.type = type;
    }

    public void setEntityId(int entityId) {
        this.isChanged = true;
        this.entityId = entityId;
    }

    public void setPlayerId(long playerId) {
        this.isChanged = true;
        this.playerId = playerId;
    }

    public void setDestination(ArrayList<Vector3f> destination) {
        this.isChanged = true;
 
    }

    public void setSpeed(float speed) {
        this.isChanged = true;
        this.speed = speed;
    }

    public void setDirection(Vector3f direction) {
        this.isChanged = true;
        this.direction = direction;
    }

    public void setIsNewCreated(boolean isNewCreated) {
        this.isChanged = true;
        this.isNewCreated = isNewCreated;
    }

    public void setCollided(boolean collided) {
        this.isChanged = true;
        this.collided = collided;
    }

    public void setLastPosition(Vector3f lastPosition) {
        this.isChanged = true;
        this.lastPosition = lastPosition;
    }

    public void setName(String name) {
        this.isChanged = true;
        this.name = name;
    }

    public void setSize(float size) {
        this.isChanged = true;
        this.size = size;
    }

    public void setHasStaticPosition(boolean hasStaticPosition) {
        this.isChanged = true;
        this.hasStaticPosition = hasStaticPosition;
    }

    public void setCollisionTypes(CollisionTypes collisionTypes) {
        this.isChanged = true;
        this.collisionTypes = collisionTypes;
    }

    public void setIsBuildable(boolean isBuildable) {
        this.isChanged = true;
        this.isBuildable = isBuildable;
    }

    public void setIsMoveable(boolean isMoveable) {
        this.isChanged = true;
        this.isMoveable = isMoveable;
    }

    public void setActiveOrder(Order activeOrder) {
        this.isChanged = true;
        this.activeOrder = activeOrder;
    }

    public void setIsMoving(boolean isMoving) {
        this.isChanged = true;
        this.isMoving = isMoving;
    }

    public Vector3f getPosition() {
        return position;
    }

    public EntityTypes getType() {
        return type;
    }

    public int getEntityId() {
        return entityId;
    }

    public long getPlayerId() {
        return playerId;
    }


    
  

    public float getSpeed() {
        return speed;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public boolean isIsNewCreated() {
        return isNewCreated;
    }

    public boolean isCollided() {
        return collided;
    }

    public Vector3f getLastPosition() {
        return lastPosition;
    }

    public String getName() {
        return name;
    }

    public float getSize() {
        return size;
    }

    public boolean isHasStaticPosition() {
        return hasStaticPosition;
    }

    public CollisionTypes getCollisionTypes() {
        return collisionTypes;
    }

    public boolean isIsBuildable() {
        return isBuildable;
    }

    public boolean isIsMoveable() {
        return isMoveable;
    }

    public Order getActiveOrder() {
        return activeOrder;
    }

    public boolean isIsMoving() {
        return isMoving;
    }

}
