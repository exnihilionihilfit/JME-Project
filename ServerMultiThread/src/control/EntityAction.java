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

        // get node data
        Vector3f destination = entity.destination;
        float speed = entity.speed;
        long updateTime = entity.lastMoveUpdate;
        
        if(destination != null && speed != 0f){

        // convert passed time since last update to seconds (speed is length per second)
        float timePassedInSeconds = ((float) (System.currentTimeMillis() - updateTime)) / 1000.0f;

        // rotate to new direction
        Vector3f distanceToTarget = destination.subtract(entity.position);
        Vector3f directionToTarget = distanceToTarget.normalize();

        float distanceToTargetAsNumber = distanceToTarget.length();

        //calculate posible fly length this tick            
        float flyedLengthSinceLastUpdate = speed * timePassedInSeconds;

        Vector3f newPositionOnTheWay = directionToTarget.mult(flyedLengthSinceLastUpdate);

        // check if near and check if next step goes further then the target, because length is allways positiv
        if (0.01 > distanceToTargetAsNumber || distanceToTargetAsNumber - flyedLengthSinceLastUpdate < 0) {
            entity.position = destination;
            entity.moveToPositon = false;

          
            System.out.println(" arrived ");
        } else {
            entity.position = entity.position.add(newPositionOnTheWay);
            entity.lookAt = destination;


        }

       entity.lastMoveUpdate = System.currentTimeMillis();
        }
        else
        {
            System.out.println("destination: "+destination);
        }
    }
}
