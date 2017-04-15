/**
 * Action are GamesState actions in particular and will be called only from 
 * GameState class
 * 
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

    /*
    get the pick point and the geometry if one is there (-1 indicates that the transparent floor is hit)
    */

    public static int selectEntity(InputManager inputManager, Camera cam, Node rootNode) 
    {     
        CollisionResults results = PickCollision.getCollisionResults(inputManager, cam, rootNode);      
        return getEntityIDFromSelection(results);
    }
    /*
    sends the entity and the picked position to the server to validate it and the server will send it back
    */

    public static void sendEntityMoveAction(SendNetworkMessage sendNetworkMessage, Entity entity, Vector3f position)
    {        
        sendNetworkMessage.sendEntityPositionMessage(entity, position);     
    }

    /*
     a taret could the 'floor' or a other ship. The pick point on the ship could be needed in particular 
    and other information about the target
    */
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
