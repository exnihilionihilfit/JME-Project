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
import static control.state.enums.StatesEnum.PROGRAM_RUNNING;

import main.Main;

/**
 *
 * @author novo
 */
public class ProgramState extends State {

    
    private static GameState gameState;
    private final StartUp startUp;
    private final Run run;
    private static StatesEnum current;
    

    public ProgramState(Main main, InputManager inputManager, Node rootNode, Camera cam) {
        
        this.main = main;
        this.inputManager = inputManager;
        this.camera = cam;
        this.rootNode = rootNode;
        
        startUp = new StartUp();
        run = new Run();
        current = StatesEnum.PROGRAM_STARTUP;

    }

    @Override
    public boolean execute() {

        switch (current) {
            case PROGRAM_STARTUP:
                startUp.execute();
                break;
            case PROGRAM_RUNNING:
                run.execute();
                break;
            case PROGRAM_ERROR:
                //TODO
                break;
            case PROGRAM_PAUSE:
                //TODO
            case PROGRAM_SHUTDOWN:
                System.exit(1);
                
            default:
                return false;

        }
        return true;

    }

    private static class StartUp extends State {

        @Override
        public boolean execute() {
            gameState = new GameState(this.main, this.inputManager, this.rootNode, this.camera);
            StatesEnum statesEnum = StatesEnum.PROGRAM_RUNNING;
            
            changeState(statesEnum);
            return true;

        }

        @Override
        public boolean changeState(StatesEnum state) {
            ProgramState.current = PROGRAM_RUNNING;
            return true;
        }

    }
    
    private static class Run extends State{
               @Override
        public boolean execute() {
            // start new state-mashine
            gameState.execute();
            return true;

        }


        @Override
        public boolean changeState(StatesEnum state) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    

   
        
    }
    
    private static class Pause extends State{
        
        @Override
        public boolean execute() {
 
            return true;

        }
      @Override
        public boolean changeState(StatesEnum state) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
