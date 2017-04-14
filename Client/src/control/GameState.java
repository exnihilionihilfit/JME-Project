/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import javax.swing.JInternalFrame;
import main.Main;
import model.Entity;
import model.Target;

/**
 *
 * @author novo
 */
public class GameState {

    public static boolean IS_RUNNING = false;
    public static boolean IS_PAUSED = false;

    public static boolean IS_ENTITY_SELECTED = false;
    public static boolean SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;

    public static boolean IS_CLIENT_CONNECTED = false;

    private static Main main;
    private static InputManager inputManager;
    private static Camera cam;
    private static Node rootNode;

    private static Entity selectedEntity;
    private static Target target;
    private int entityID;
    private boolean isDone;

    public GameState(Main main, InputManager inputManager, Node rootNode, Camera cam) {
        GameState.main = main;
        GameState.inputManager = inputManager;
        GameState.cam = cam;
        GameState.rootNode = rootNode;
    }

    public void checkAction() {

        if (IS_CLIENT_CONNECTED) {

            if (IS_RUNNING) {

                if (IS_PAUSED) {
                    // pause
                } else {

                    /**
                     * check start condition If the entityID > -1 an entity was
                     * found if so start EntityAction cancle with rightMouse
                     *
                     * Note: the id is a fild set in enty and binde to the node
                     * we got the geometry wich is a child of the entity node so
                     * we have to "getParent" as long as we finde the node with
                     * the id entry
                     */
                    if (IS_ENTITY_SELECTED == false) {

                        if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                            entityID = Action.selectEntity(inputManager, cam, rootNode);
                            InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;
                        }
                       

                        /**
                         * get entity with id
                         */
                        if (selectedEntity == null) {
                            selectedEntity = Helper.getEntityByID(entityID);

                            if (selectedEntity != null) {

                                Action.highlight(selectedEntity);
                                IS_ENTITY_SELECTED = true;
                                isDone = true;

                                System.out.println("Entity selected");
                            } 
                        }
                    }

                    if (IS_ENTITY_SELECTED && !isDone) {

                        /**
                         * try to get an target we need the target center point
                         * pick point and target id if an entity was picked. To
                         * do so we use a target object to gather all needed
                         * info
                         */
                        if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                            target = Action.selectTargetPosition(inputManager, cam, rootNode);
                            InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;

                            if (target != null) {
                                SEND_ENTITY_MOVE_ACTION_TO_SERVER = true;

                            }
                            if (SEND_ENTITY_MOVE_ACTION_TO_SERVER) {

                                Vector3f entityPositionTarget = target.getPickPoint();

                                System.out.println("ENTIY " + selectedEntity);

                                Action.sendEntityMoveAction(main.sendNetworkMessage, selectedEntity, entityPositionTarget);

                                System.out.println("target found!" + target.getPickPoint());

                                IS_ENTITY_SELECTED = false;
                                SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;
                                selectedEntity = null;
                                target = null;

                            } else {
                                // System.out.println("no target found"); 
                            }
                        }

                    }

                    if (InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED) {
                        IS_ENTITY_SELECTED = false;
                        SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;
                        selectedEntity = null;
                        target = null;
                        InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;
                    }

 
                    isDone = false;
                }
            }
        }
    }

    public void updateGameState() {

        if (main.client != null) {
            IS_CLIENT_CONNECTED = true;
        } else {
            IS_CLIENT_CONNECTED = false;
        }

        IS_RUNNING = main.isRunning();

    }
}
