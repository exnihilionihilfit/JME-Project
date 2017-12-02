/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.finateStateMachine;

import static control.finateStateMachine.State.NULL_STATE;
import java.util.ArrayDeque;
import java.util.Deque;
import main.Main;

public class StackFSM <T>{
    private final Deque<State> stack ;
    private final T t;
    private final Main main;
    
    /**
     * To be able to use the StackFiniteStateMachine with
     * any object. Mainly used with Entities
     * @return object with generic type <t>
     */
    public T getT()
    {
        return t;
    }
    /**
     * 
     * @param t
     * @param main 
     */
 
    public StackFSM(T t, Main main) {
        this.stack = new ArrayDeque<>();
        this.t = t;
        this.main = main;
    }
    
    public Main getMain()
    {
        return this.main;
    }
 
    public void update(float tpf) {
        State currentState  = getCurrentState();
 
        if (currentState != null) {
            currentState.update(tpf,this);
        }
    }
     
    public State popState() {
      
        return stack.pop();
    }
 
    public void pushState(State state) {
        if (getCurrentState() != state) {
            stack.push(state);
            
        }
    }
    
    public void changeState(State next)
    {
        
        if(!getCurrentState().equals(next))
        {
            getCurrentState().leave(this);
            pushState(next);
            next.enter(this);
        }
    }
 
    public State getCurrentState() {
        
        State tmp = stack.peek();
        
        if(tmp != null){
            return tmp;
        }
        else
        {
            return NULL_STATE;
        }
    }
}