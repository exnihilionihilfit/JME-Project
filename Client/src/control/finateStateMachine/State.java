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
     */
    protected abstract void enter(StackFSM stackFSM);

    /**
     * Called by the StateMachine when leave this State.
     */
    protected abstract void leave(StackFSM stackFSM);
    
    

    /**
     * 
     */
    public static final State NULL_STATE = new State("NULL_STATE") {
        @Override
        public void update(float tpf,StackFSM stackFSM) {
        }

        @Override
        protected void leave(StackFSM stackFSM) {
            throw new UnsupportedOperationException("NULL_STATE"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void enter(StackFSM stackFSM) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

}
