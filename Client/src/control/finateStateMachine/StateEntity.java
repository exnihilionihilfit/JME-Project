/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.finateStateMachine;

import model.Entity;
import model.Player;

/**
 *
 * @author novo
 */
public class StateEntity {

   

    public static final State SELECTED = new State("SELECTED") {
        @Override
        protected void update(float tpf, StackFSM stackFSM) {
//      
        }

        @Override
        protected void enter(StackFSM stackFSM) {
            
            Entity entity = (Entity) stackFSM.getT();
            
            if (entity != null) {
                entity.addHighlight(Player.getPlayerId());

                System.out.println("Entity selected \n name: " + entity.getName()
                        + " playerId: " + entity.getPlayerId()
                        + " entityId: " + entity.getID());
            }

        }

        @Override
        protected void leave(StackFSM stackFSM) {
            Entity entity = (Entity) stackFSM.getT();
        
            if (entity != null) {
             

                    entity.removeHighLight();
                    stackFSM.popState();

                
            }
        }

    };

    /**
     *
     */
    public static final State MOVE = new State("MOVE") {
        @Override
        public void update(float tpf, StackFSM stackFSM) {
        }

        @Override
        protected void enter(StackFSM stackFSM) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void leave(StackFSM stackFSM) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

    /**
     *
     */
    public static final State HOLD = new State("HOLD") {

        @Override
        public void update(float tpf, StackFSM stackFSM) {
            // if move order change to state MOVE
            //

        }

        @Override
        protected void leave(StackFSM stackFSM) {

        }

        @Override
        protected void enter(StackFSM stackFSM) {

        }
    };

}
