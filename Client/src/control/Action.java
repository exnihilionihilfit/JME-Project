/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import control.network.SendNetworkMessage;
import model.Entity;
import model.Target;

/**
 *
 * @author novo
 */
public class Action {

    

    public static int selectEntity(InputManager inputManager, Camera cam, Node rootNode) 
    {     
        CollisionResults results = PickCollision.getCollisionResults(inputManager, cam, rootNode);      
        return getEntityIDFromSelection(results);
    }

    public static void sendEntityMoveAction(SendNetworkMessage sendNetworkMessage, Entity entity, Vector3f position)
    {        
        sendNetworkMessage.sendEntityPositionMessage(entity, position);     
    }

    public static Target selectTargetPosition(InputManager inputManager, Camera cam, Node rootNode) {
        Target target = null;
        
        CollisionResults results = PickCollision.getCollisionResults(inputManager, cam, rootNode);
        
        
        if (results.size() > 0) {
            Vector3f contactPoint = results.getClosestCollision().getContactPoint();
            
            if(contactPoint != null)
            {
            target = new Target();
            target.setPickPoint(contactPoint);
            }
        
        }
        
      
        
        return target;
    }

    
    private static int getEntityIDFromSelection(CollisionResults results)
    {
        // -1 is floor
        int selectedEntityID = -1;
        
            if (results.size() > 0) {
                // strange but work
                Node node = results.getClosestCollision().getGeometry().getParent();
                if(node != null)
                {
                    node = node.getParent();
                    if(node != null)
                    {
                        node = node.getParent();
                        String name = node.getName();
                        
                        if(name != null && name.equals("ship"))
                        {
                          selectedEntityID = node.getUserData("id");
                        }                            
                    }                
                }                
            }            
         return selectedEntityID;
    }

    static void highlight(Entity selectedEntity) {

    }

}
