/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.state;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import control.state.enums.StatesEnum;


import main.Main;

/**
 *
 * @author novo
 */
public class GameState extends ProgramState 
{
    
    public GameState(Main main, InputManager inputManager, Node rootNode, Camera cam) {
        super(main, inputManager, rootNode, cam);
    }
    
    
    @Override
    public boolean execute() {
        
        return false;
    }
     @Override
    public boolean changeState(StatesEnum state){
        
        return false;
    }
    
    
    
}
