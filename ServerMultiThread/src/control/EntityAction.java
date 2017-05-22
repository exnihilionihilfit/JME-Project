/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import model.EntityContainer;

/**
 *
 * @author novo
 */
public class EntityAction {

    public static void moveEntityToPosition(EntityContainer entity) {

        if (entity.activeOrder.type == OrderTypes.MOVE) {
            // get node data
            Vector3f destination = entity.activeOrder.destination;
            float speed = entity.speed;
            long updateTime = entity.activeOrder.lastExecution;
            entity.lastPosition = entity.position;

            if (destination != null && speed != 0f) {

                // convert passed time since last update to seconds (speed is length per second)
                float timePassedInSeconds = ((float) (System.currentTimeMillis() - updateTime)) / 1000.0f;

                // rotate to new direction
                Vector3f distanceToTarget = destination.subtract(entity.lastPosition);
                Vector3f directionToTarget = distanceToTarget.normalize();

                float distanceToTargetAsNumber = distanceToTarget.length();

                //calculate posible fly length this tick            
                float flyedLengthSinceLastUpdate = speed * timePassedInSeconds;

                Vector3f newPositionOnTheWay = directionToTarget.mult(flyedLengthSinceLastUpdate);

                // check if near and check if next step goes further then the target, because length is allways positiv
                if (0.01 > distanceToTargetAsNumber || distanceToTargetAsNumber - flyedLengthSinceLastUpdate < 0) {
                    entity.position = destination;
                    entity.activeOrder.orderDone(OrderTypes.MOVE);

                    System.out.println(" arrived ");
                } else {
                    entity.position = entity.position.add(newPositionOnTheWay);
                    entity.direction = directionToTarget;
                }

                entity.activeOrder.lastExecution = System.currentTimeMillis();
            } else {
                System.out.println("destination: " + destination);
            }
        }
    }
    
    public static void executeOrder(EntityContainer entity) {
        switch (entity.activeOrder.type){
            case STAY:
                break;
            case MOVE:
                moveEntityToPosition(entity);
                break;
            case NOT_DEFINED:
                System.out.println("undefined Order ");
                entity.activeOrder = new Order(entity.playerId, entity.entityId, entity.position, OrderTypes.STAY);
                break;
            default:
                System.out.println("hit defaultState execute Order");
                break;
        }
    }
}
