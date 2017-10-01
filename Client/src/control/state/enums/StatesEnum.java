/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.state.enums;

/**
 *
 * @author novo
 */
public enum StatesEnum {

    PROGRAM_STARTUP(),
    PROGRAM_RUNNING(),
    PROGRAM_PAUSE(),
    PROGRAM_SHUTDOWN(),
    PROGRAM_ERROR(),
    
    GAME_IDLE(),
    GAME_HUD(),
    GAME_FIELD();

}
