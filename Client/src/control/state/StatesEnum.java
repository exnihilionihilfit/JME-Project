/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.state;

/**
 *
 * @author novo
 */
public final class StatesEnum {
    

    
   public enum Program {
        STARTUP(),
        RUNNING(),
        PAUSE(),
        SHUTDOWN(),
        ERROR();
      
        
    }

 public enum Game{
            IDLE(),
            HUD(),
            FIELD();
        }
    
    
}
