/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author chasma
 */
public class Entity {

    private void move(Spatial ship) {

        // get node data
        Vector3f destination = ship.getUserData("destination");
        float speed = ship.getUserData("speed");
        long updateTime = ship.getUserData("lastMoveUpdate");

        // convert passed time since last update to seconds (speed is length per second)
        float timePassedInSeconds = ((float) (System.currentTimeMillis() - updateTime)) / 1000.0f;

        // rotate to new direction
        Vector3f distanceToTarget = destination.subtract(ship.getLocalTranslation());
        Vector3f directionToTarget = distanceToTarget.normalize();

        float distanceToTargetAsNumber = distanceToTarget.length();

        //calculate posible fly length this tick            
        float flyedLengthSinceLastUpdate = speed * timePassedInSeconds;

        Vector3f newPositionOnTheWay = directionToTarget.mult(flyedLengthSinceLastUpdate);

        // check if near and check if next step goes further then the target, because length is allways positiv
        if (0.01 > distanceToTargetAsNumber || distanceToTargetAsNumber - flyedLengthSinceLastUpdate < 0) {
            ship.setLocalTranslation(destination);
            ship.setUserData("moveTo", false);

            RigidBodyControl shipControll = ship.getControl(RigidBodyControl.class);

            if (shipControll != null) {
                shipControll.setPhysicsLocation(ship.getLocalTranslation());
                shipControll.setPhysicsRotation(ship.getLocalRotation());
            }

            System.out.println(" arrived ");
        } else {
            ship.setLocalTranslation(ship.getLocalTranslation().add(newPositionOnTheWay));
            ship.lookAt(destination, Vector3f.UNIT_Y);

            RigidBodyControl shipControll = ship.getControl(RigidBodyControl.class);

            if (shipControll != null) {
                shipControll.setPhysicsLocation(ship.getLocalTranslation());
                shipControll.setPhysicsRotation(ship.getLocalRotation());
            }

            // System.out.println(" flying to "+directionToTarget);
        }

        ship.setUserData("lastMoveUpdate", System.currentTimeMillis());

    }

}
