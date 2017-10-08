/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.finateStateMachine;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import control.Action;
import control.CreateEntityGeometry;
import control.InputListener;
import static main.Main.main;
import model.Entity;
import model.EntityTypes;
import model.Target;
import static org.slf4j.LoggerFactoryFriend.reset;
import view.HUD;

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
            Entity entity = (Entity) stackFSM.getT();
            stackFSM.popState();
        }

        @Override
        protected void enter(StackFSM stackFSM) {

            Entity entity = (Entity) stackFSM.getT();

            if (entity.isMoveable()) {

                Target target = Action.selectTargetPositionOnFloor(stackFSM.getMain().getInputManager(), stackFSM.getMain().getCamera(), stackFSM.getMain().getRootNode());
                InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;

                if (target != null) {

                    Vector3f entityPositionTarget = target.getPointOnFloor();
                    Action.sendEntityMoveAction(stackFSM.getMain().sendNetworkMessage, entity, entityPositionTarget);

                    System.out.println("ENTIY " + entity);
                    System.out.println("target found!" + target.getContactPoint());

                }
            }
        }

        @Override
        protected void leave(StackFSM stackFSM) {

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
    /**
     * This is for building structures first check if the selected entity could
     * build buildings then check if one building is selected by button
     *
     * then create a geometry and add it to the rootNode as indicator for
     * building
     */

    public static final State SHOW_MENU = new State("SHOW_MENU") {

        EntityTypes TO_BUILD_ENTITY_TYPE = EntityTypes.NOT_DEFINED;
        Node buildingPlacementHull = null;
        boolean MOVE_PLACEMENT_HULL = false;
        Target placementTarget;

        @Override
        protected void update(float tpf, StackFSM stackFSM) {
            
            if (TO_BUILD_ENTITY_TYPE.equals(EntityTypes.NOT_DEFINED)) {
                if (HUD.IS_BUILD_EXCHANGE_STATION) {
                    TO_BUILD_ENTITY_TYPE = EntityTypes.EXCHANGE_STATION;
                } else if (HUD.IS_BUILD_SENSOR_STATION) {
                    TO_BUILD_ENTITY_TYPE = EntityTypes.SENSOR_STATION;
                } else if (HUD.IS_BUILD_SKIFF) {
                    TO_BUILD_ENTITY_TYPE = EntityTypes.SKIFF;
                } else {
                    TO_BUILD_ENTITY_TYPE = EntityTypes.NOT_DEFINED;
                }
            } else {

                if (buildingPlacementHull == null) {

                    buildingPlacementHull = CreateEntityGeometry.getEntityNode(TO_BUILD_ENTITY_TYPE, -2, stackFSM.getMain().getAssetManager());
                    stackFSM.getMain().getRootNode().attachChild(buildingPlacementHull);
                    MOVE_PLACEMENT_HULL = true;
                }

                if (MOVE_PLACEMENT_HULL) {
                    placementTarget = Action.selectTargetPositionOnFloor(stackFSM.getMain().getInputManager(), stackFSM.getMain().getCamera(), stackFSM.getMain().getRootNode());
                    placementTarget.getPointOnFloor().y = 0f;

                    buildingPlacementHull.setLocalTranslation(placementTarget.getPointOnFloor());

                    if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                        Action.sendCreateEntity(stackFSM.getMain().sendNetworkMessage, TO_BUILD_ENTITY_TYPE.name(), TO_BUILD_ENTITY_TYPE, placementTarget.getPointOnFloor());
                        System.out.println("new building placed");
                        stackFSM.popState();
                    }

                }

            }
        }

        @Override
        protected void enter(StackFSM stackFSM) {
            Entity entity = (Entity) stackFSM.getT();

            HUD.IS_BUILDABLE(true);

        }

        @Override
        protected void leave(StackFSM stackFSM) {

            if (buildingPlacementHull != null) {
                stackFSM.getMain().getRootNode().detachChild(buildingPlacementHull);
            }
           
            buildingPlacementHull = null;
            TO_BUILD_ENTITY_TYPE = EntityTypes.NOT_DEFINED;
        }

    };

}
