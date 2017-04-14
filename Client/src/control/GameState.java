/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import main.Main;
import model.Entity;

/**
 *
 * @author novo
 */
public class GameState {
    
    public static boolean IS_RUNNING = false;
    public static boolean IS_PAUSED = false;
    
    public static boolean IS_ENTITY_SELECTED = false;
    public static boolean IS_ENTITY_MOVE_ACTION = false;
    
    public static boolean IS_CLIENT_CONNECTED = false;
    
    private static Main main;
    private static InputManager inputManager;
    private static Camera cam;
    private static Node rootNode;
    
    private Entity selectedEntity;
    private Vector3f entityPositionTarget;
    
    public GameState(Main main, InputManager inputManager, Node rootNode, Camera cam)
    {
        GameState.main = main;
        GameState.inputManager = inputManager;
        GameState.cam = cam;
        GameState.rootNode = rootNode;
    }

    
    public void checkAction()
    {
        if(IS_CLIENT_CONNECTED)
        {
        if(IS_RUNNING)
        {
            
            if(IS_PAUSED)
            {
                // pause
            }
            else
            {
                if(IS_ENTITY_SELECTED)
                {
                    if(IS_ENTITY_MOVE_ACTION)
                    {
                        Action.entityMoveAction(main.sendNetworkMessage, selectedEntity, entityPositionTarget);
                    }
                }
                
                Action.selectEntity(inputManager, cam, rootNode);
                
              
            }
            
        }
        }
    }
    
    public  void updateGameState()
    {
        if(main.client != null)
        {
            IS_CLIENT_CONNECTED = true;
        }
        else{
            IS_CLIENT_CONNECTED = false;
        }
        
        IS_RUNNING = main.isRunning();
        
        
        if(selectedEntity != null && entityPositionTarget != null)
        {
            IS_ENTITY_MOVE_ACTION = true;
        }
        else
        {
            IS_ENTITY_MOVE_ACTION = false;
        }
       
         
    }
    
}
