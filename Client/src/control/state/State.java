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
import control.state.interfaces.StateHandling;
import main.Main;



/**
 *
 * @author novo
 */
public abstract class State implements StateHandling{
    
    protected Main main;
    protected InputManager inputManager;
    protected Camera camera;
    protected Node rootNode;
    
    


    @Override
    public boolean changeState(StatesEnum state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 

    
}
