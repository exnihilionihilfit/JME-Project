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
 * @author novo
 */
public class SimpleCollision {
    
    private static final float MININAL_DISTANCE = 10.0f;
    
    public static void checkCollision(EntityContainer container, ArrayList<EntityContainer> enityContainers)
    {
        for(EntityContainer entityContainer: enityContainers)
        {
            entityContainer.collided = false;
        }
                        
            for(EntityContainer entityContainerCopy:enityContainers)
            {
                if(container.entityId != entityContainerCopy.entityId)
                {
                    Vector3f tmp = container.position.subtract(entityContainerCopy.position); 
                    
                    float distance = tmp.length();
               
                    if(distance <= MININAL_DISTANCE )
                    {
                        reactOnCollision(container, entityContainerCopy);                    
                    }                
                }
            }
        
    }
    
    private static void reactOnCollision(EntityContainer a, EntityContainer b) {
                 
        a.position = a.lastPosition;
        
       
            
            Vector3f distance = a.position.subtract(b.position);
            
             if(distance.length() < MININAL_DISTANCE )
             {
                 Vector3f direction = distance.normalize();
                 
                 if(direction.equals(Vector3f.ZERO))
                 {
                     direction = Vector3f.UNIT_X;
                 }
                 
                 Vector3f setBack = direction.negate().mult(3);
               
                 a.position.addLocal(setBack);
                 b.position.addLocal(setBack.negate());
               
             }
         
    }

}
