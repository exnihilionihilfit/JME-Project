/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import model.EntityContainer;

/**
 *
 * @author Antarius, novo
 */
public class SimpleCollision {

    public static void resetCollided(ArrayList<EntityContainer> enityContainers) {
        for (EntityContainer entityContainer : enityContainers) {
            entityContainer.setCollided(false);
        }
    }

    public static boolean checkCollision(EntityContainer container, ArrayList<EntityContainer> enityContainers) {

        for (EntityContainer entityContainerCopy : enityContainers) {

        
            
            if (container.getEntityId() != entityContainerCopy.getEntityId()) {
                Vector3f tmp = container.getPosition().subtract(entityContainerCopy.getPosition());

                float distance = tmp.length();

                if (distance < container.getSize()) {
                    if (container.getCollisionTypes().equals(CollisionTypes.MOVEABLE)) {
                        primitiveReactOnCollision(container, entityContainerCopy, tmp);                       
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void primitiveReactOnCollision(EntityContainer a, EntityContainer b, Vector3f vectorAminusB) {

        /**
         * @author Antarius ToDo: - nonmovable objects - own collision for them
         * - collision damage - move methode in enteties
         */
        ///a.position = a.lastPosition;       
        float distance = vectorAminusB.length();

        if (distance < a.getSize()) {
            Vector3f direction = vectorAminusB.normalize();

            if (direction.equals(Vector3f.ZERO)) {
                direction = Vector3f.UNIT_X;
            }

            float closeness = (a.getSize() - distance) /*  / 2 +1   */; //still problems with 3rd dimention
            Vector3f setBack = direction.mult(closeness);
            setBack.y = 0;

            a.getPosition().addLocal(setBack);
            a.setCollided(true);
            
            // only if the entity is moveable the collision will be set to 
            // trigger a reset to client and update position
           if (b.getCollisionTypes().equals(CollisionTypes.MOVEABLE)) {
            b.getPosition().addLocal(setBack.negate());
            b.setCollided(true);
           }
        }

    }

  

}
