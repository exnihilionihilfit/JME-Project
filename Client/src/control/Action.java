/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import control.network.SendNetworkMessage;
import model.Entity;

/**
 *
 * @author novo
 */
public class Action {
    
    public static void selectEntity(InputManager inputManager, Camera cam, Node rootNode)
    {
 
       // Reset results list.
       CollisionResults results = new CollisionResults();
       // Convert screen click to 3d position
       Vector2f click2d = inputManager.getCursorPosition();
       Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
       Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d);
       // Aim the ray from the clicked spot forwards.
       Ray ray = new Ray(click3d, dir);
       // Collect intersections between ray and all nodes in results list.
       rootNode.collideWith(ray, results);
       // (Print the results so we see what is going on:)
       for (int i = 0; i < results.size(); i++) {
         // (For each "hit", we know distance, impact point, geometry.)
         float dist = results.getCollision(i).getDistance();
         Vector3f pt = results.getCollision(i).getContactPoint();
         String target = results.getCollision(i).getGeometry().getName();
         System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
       }
       // Use the results -- we rotate the selected geometry.
       if (results.size() > 0) {
         
       }
       
     }

    
    
    public static void entityMoveAction(SendNetworkMessage sendNetworkMessage, Entity entity, Vector3f position)
    {
        if(InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED)
        {
             sendNetworkMessage.sendEntityPositionMessage(entity, position);
        }
        else if(InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED)
        {
            GameState.IS_ENTITY_MOVE_ACTION = false;
        }
    }
    
 
    
}
