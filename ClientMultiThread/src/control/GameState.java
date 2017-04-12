/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author novo
 */
public class GameState {
    
    public static boolean IS_RUNNING = false;
    public static boolean IS_PAUSED = false;
    
    public static boolean IS_ENTITY_SELECTED = false;
    
    
    public void gameState()
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
                    // if the game is running and a entity is selected 
                    // get entity data and wait for action input
                }
            }
            
        }
    }
    
}
