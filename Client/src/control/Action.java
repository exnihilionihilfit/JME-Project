/**
 * Action are GamesState actions in particular and will be called only from
 * GameState class
 *
 */
package control;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Plane;
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

    private static final Plane FLOOR_PLANE = new Plane(Vector3f.UNIT_Y, 0);

    /*
    get the pick point and the geometry if one is there (-1 indicates that the transparent floor is hit)
     */
    public static int selectEntity(InputManager inputManager, Camera cam, Node rootNode) {
        CollisionResults results = PickCollision.getCollisionResults(inputManager, cam, rootNode);
        return getEntityIDFromSelection(results);
    }

    /*
    sends the entity and the picked position to the server to validate it and the server will send it back
     */
    public static void sendEntityMoveAction(SendNetworkMessage sendNetworkMessage, Entity entity, Vector3f position) {
        sendNetworkMessage.sendEntityPositionMessage(entity, position);
    }

    public static void sendCreateEntity(SendNetworkMessage sendNetworkMessage) {
        sendNetworkMessage.sendCreateEntityMessage();
    }

    /*
     a target could the 'floor' or a other ship. The pick point on the ship could be needed in particular 
    and other information about the target
     */
    public static Target selectTargetPositionOnFloor(InputManager inputManager, Camera cam, Node rootNode) {
        Target target = null;

        CollisionResults results = PickCollision.getCollisionResults(inputManager, cam, rootNode);

        /**
         * a entity or the floor could be hit in each case we need to project
         * the pick point to the floor plain
         */
        if (results.size() > 0) {

            Vector3f contactPoint = results.getClosestCollision().getContactPoint();
            /**
             * we use the plane to project the pick point to it because the pick
             * point could be on top of an entity TODO: use the entity to create
             * a plane as base so 3d movement is possible but not now ;)
             *
             */
            Vector3f pointOnFloor = FLOOR_PLANE.getClosestPoint(contactPoint);

            if (contactPoint != null) {

                target = new Target();
                target.setPointOnFloor(pointOnFloor);
                target.setContactPoint(contactPoint);

            }

        }

        return target;
    }

    private static int getEntityIDFromSelection(CollisionResults results) {
        // -1 is floor
        int selectedEntityID = -1;
        if (results.size() > 0) {
            
            /**
             * go through all collison results and
             * fetch hitted geometry by ray cast
             * check name of node and go up to root node
             * if any node called entity
             * return its id
             */
            
            for (CollisionResult result : results) {                             

               
                Node node = result.getGeometry().getParent();
                
                while(node != null)
                {                    
                    if(node.getName().equals("entity"))
                    {
                        System.out.println(node.getName());
                         return node.getUserData("id");                           
                    }                    
                    node = node.getParent();
                }    
            }
        }
        return selectedEntityID;
    }

}
