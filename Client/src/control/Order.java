/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Antarius
 */
@Serializable
public class Order {
    public OrderTypes type = OrderTypes.NOT_DEFINED;
    public Vector3f destination = new Vector3f(1, 0, 0);
    public ArrayList<Vector3f> path = new ArrayList<>();
    public int entityID = -1;
    public int targetEntityID = -1;
    public long payerId = -1;
    public Vector3f orientation = new Vector3f(1, 0, 0);
    public long lastExecution = -1;
    
    public Order() {
        type = OrderTypes.NOT_DEFINED;
    }
    
    // ToDo - direction could be cut out
    //      - check for Ordertype by creation
    
    // constructor for Move-Order      
    public Order(long playerId, int entityID, Vector3f destination, Vector3f direction, OrderTypes type) {
        this.payerId = playerId;
        this.destination = destination;
        this.entityID = entityID;
        this.orientation = direction;
        this.type = type;
        this.lastExecution = System.currentTimeMillis();
    }
    
    // constructor for MoveToObject-Order
    public Order(long playerId, int entityID, int targetEntityID, Vector3f direction, OrderTypes type) {
        this.payerId = playerId;        
        this.entityID = entityID;
        this.targetEntityID = targetEntityID;
        this.orientation = direction;
        this.type = type;
        this.lastExecution = System.currentTimeMillis();
    }
    
    // constructor for Stay-Order, default state for all entities
    public Order(long playerId, int entityID, Vector3f destination, OrderTypes type){
        if(type == OrderTypes.STAY){
            this.payerId = playerId;
            this.destination = destination;
            this.entityID = entityID;
            this.type = type;
            //this.lastExecution = System.currentTimeMillis(); is this needed?
        }
        else{
            this.type = OrderTypes.NOT_DEFINED;
        }
        
    }
    
    /*
    // constructor for Mining-Order
    public Order(long playerId, int entityID, Vector3f destination, Vector3f direction, OrderTypes type) {
        this.payerId = playerId;
        this.destination = destination;
        this.entityID = entityID;
        this.orientation = direction;
        this.type = type;
        this.lastExecution = System.currentTimeMillis();
    }
    */
    public void orderDone(OrderTypes type){
        if(this.type == type){
            this.type = OrderTypes.STAY;
        }else{
        }    
    }
    
    // return true if it is a real Order, so neither STAY or NOT_Defined
    public boolean isActive(){
        if(type == OrderTypes.STAY || type == OrderTypes.NOT_DEFINED){
            return false;
        }else{
            return true;
        }
    }
}
