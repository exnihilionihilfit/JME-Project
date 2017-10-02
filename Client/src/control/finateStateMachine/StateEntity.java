/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.finateStateMachine;

import com.jme3.math.Vector3f;
import control.Action;
import static control.GameState.SEND_ENTITY_MOVE_ACTION_TO_SERVER;
import control.InputListener;
import model.Entity;
import model.Target;

/**
 *
 * @author novo
 */
public class StateEntity {

    /**
     *
     */
    public static final State MOVE = new State("MOVE") {
        @Override
        public void update(float tpf, StackFSM stackFSM) {
        }

        @Override
        protected void enter(StackFSM stackFSM) {

            Entity entity = (Entity) stackFSM.getT();
            
            if(entity.isMoveable()){

            Target target = Action.selectTargetPositionOnFloor(stackFSM.getMain().getInputManager(), stackFSM.getMain().getCamera(), stackFSM.getMain().getRootNode());
            InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;

            if (target != null) {

                    Vector3f entityPositionTarget = target.getPointOnFloor();
                    Action.sendEntityMoveAction(stackFSM.getMain().sendNetworkMessage, entity, entityPositionTarget);

                    System.out.println("ENTIY " + entity);
                    System.out.println("target found!" + target.getContactPoint());
                 
                }
            
            }
            stackFSM.popState();
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
