/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.state.interfaces;


import control.state.enums.StatesEnum;

/**
 *
 * @author novo
 */
public interface StateHandling {
    
    boolean changeState(StatesEnum state);    
    boolean execute();
    
    
}
