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

   public static void resetCollided(ArrayList<EntityContainer> enityContainers)
   {
          for (EntityContainer entityContainer : enityContainers) {
            entityContainer.collided = false;
        }
   }

    public static boolean checkCollision(EntityContainer container, ArrayList<EntityContainer> enityContainers) {

     

        for (EntityContainer entityContainerCopy : enityContainers) {
            if (container.entityId != entityContainerCopy.entityId) {
                Vector3f tmp = container.position.subtract(entityContainerCopy.position);

                float distance = tmp.length();

                if (distance < container.size) {
                    primitiveReactOnCollision(container, entityContainerCopy, tmp);
                    container.collided = true;
                    entityContainerCopy.collided = true;
                    return true;
                }
            }
        }
        return false;
    }

    private static void primitiveReactOnCollision(EntityContainer a, EntityContainer b, Vector3f vectorAminusB) {

        /**
         *
         * @author Antarius ToDo: - nonmovable objects - own collision for them
         * - collision damage
         */
        a.position = a.lastPosition;

        float distance = vectorAminusB.length();

        if (distance < a.size) {
            Vector3f direction = vectorAminusB.normalize();

            if (direction.equals(Vector3f.ZERO)) {
                direction = Vector3f.UNIT_X;
            }

            float closeness = (a.size- distance) /*  / 2 +1   */; //gibt noch probleme wegen dritter dimension
            Vector3f setBack = direction.mult(closeness);

            a.position.addLocal(setBack);
            b.position.addLocal(setBack.negate());

        }

    }

}
