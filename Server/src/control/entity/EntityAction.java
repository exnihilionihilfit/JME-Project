/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.entity;

import com.jme3.math.Vector3f;
import control.Order;
import control.OrderTypes;
import model.EntityContainer;

/**
 *
 * @author novo
 */
public class EntityAction {

    public static void moveEntityToPosition(EntityContainer entity) {

        if (entity.getActiveOrder().type == OrderTypes.MOVE) {
            // get node data
            Vector3f destination = entity.getActiveOrder().destination;
            float speed = entity.getSpeed();
            long updateTime = entity.getActiveOrder().lastExecution;
            entity.setLastPosition(entity.getPosition());

            if (destination != null && speed != 0f) {

                // convert passed time since last update to seconds (speed is length per second)
                float timePassedInSeconds = ((float) (System.currentTimeMillis() - updateTime)) / 1000.0f;

                // rotate to new direction
                Vector3f distanceToTarget = destination.subtract(entity.getLastPosition());
                Vector3f directionToTarget = distanceToTarget.normalize();

                float distanceToTargetAsNumber = distanceToTarget.length();

                //calculate posible fly length this tick            
                float flyedLengthSinceLastUpdate = speed * timePassedInSeconds;

                Vector3f newPositionOnTheWay = directionToTarget.mult(flyedLengthSinceLastUpdate);

                // check if near and check if next step goes further then the target, because length is allways positiv
                if (0.01 > distanceToTargetAsNumber || distanceToTargetAsNumber - flyedLengthSinceLastUpdate < 0) {
                    entity.setPosition(destination);
                    entity.getActiveOrder().orderDone(OrderTypes.MOVE);

                    System.out.println(" arrived "+entity.getActiveOrder().type);
                } else {
                    entity.setPosition(entity.getPosition().add(newPositionOnTheWay));
             
                    entity.setDirection(directionToTarget);
                }

                entity.getActiveOrder().lastExecution = System.currentTimeMillis();
            } else {
                System.out.println("destination: " + destination);
            }
        }
    }
    
    public static void executeOrder(EntityContainer entity) {
        switch (entity.getActiveOrder().type){
            case STAY:
                break;
            case MOVE:
                moveEntityToPosition(entity);
                break;
            case NOT_DEFINED:
                System.out.println("undefined Order ");
                entity.setActiveOrder( new Order(entity.getPlayerId(), entity.getEntityId(), entity.getPosition(), OrderTypes.STAY));
                break;
            default:
                System.out.println("hit defaultState execute Order");
                break;
        }
    }
}
