/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.finateStateMachine;

/**
 *
 * @author novo
 */
public abstract class State {

    private final String name;
    StackFSM StackFSM;
  

    public State(String name) {
        this.name = name;
     
    }

    public String getName() {
        return this.name;
    }

    /**
     * Called by the StateMachine when update this Sate
     *
     * @param tpf time between two updates
     * @param stackFSM
     */
    protected abstract void update(float tpf,StackFSM stackFSM);

    /**
     * Called by the StateMachine when enter this State.
     *
     * @param stackFSM
     */
    protected abstract void enter(StackFSM stackFSM);

    /**
     * Called by the StateMachine when leave this State.
     * @param stackFSM
     */
    protected abstract void leave(StackFSM stackFSM);
    
    

    /**
     * A NULL-Object to prevent a null pointer exception.
     */
    public static final State NULL_STATE = new State("NULL_STATE") {
        @Override
        public void update(float tpf,StackFSM stackFSM) {
        }

        @Override
        protected void leave(StackFSM stackFSM) {
            
        }

        @Override
        protected void enter(StackFSM stackFSM) {
           
        }
    };

}
