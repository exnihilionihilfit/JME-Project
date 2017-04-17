/**
 * To decide in which situation the game is.
 * So we can pick an object and e.g. move it
 */
package control;

import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import main.Main;
import model.Entity;
import model.Player;
import model.Target;
import view.HUD;

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

    public GameState(Main main, InputManager inputManager, Node rootNode, Camera cam) {
        GameState.main = main;
        GameState.inputManager = inputManager;
        GameState.cam = cam;
        GameState.rootNode = rootNode;
    }

    public void entityInteraction() {

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

                                if (selectedEntity.getPlayerId() == Player.getPlayerId()) {
                                    selectedEntity.addHighlight();
                                    IS_ENTITY_SELECTED = true;

                                    System.out.println("Entity selected \n name: " + selectedEntity.getName()
                                            + " playerId: " + selectedEntity.getPlayerId()
                                            + " entityId: " + entityID);
                                } else {

                                }

                            }
                        }
                    }

                    if (IS_ENTITY_SELECTED) {

                        /**
                         * try to get an target we need the target center point
                         * pick point and target id if an entity was picked. To
                         * do so we use a target object to gather all needed
                         * info
                         */
                        if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                            target = Action.selectTargetPositionOnFloor(inputManager, cam, rootNode);
                            InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;

                            if (target != null) {
                                SEND_ENTITY_MOVE_ACTION_TO_SERVER = true;

                            }
                            if (SEND_ENTITY_MOVE_ACTION_TO_SERVER) {

                                Vector3f entityPositionTarget = target.getPointOnFloor();

                                System.out.println("ENTIY " + selectedEntity);

                                Action.sendEntityMoveAction(main.sendNetworkMessage, selectedEntity, entityPositionTarget);

                                System.out.println("target found!" + target.getContactPoint());

                                // IS_ENTITY_SELECTED = false;
                                SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;
                                //  selectedEntity = null;
                                target = null;

                            } else {
                                // System.out.println("no target found"); 
                            }
                        }

                    }

                    if (InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED) {
                        if (selectedEntity != null) {
                            selectedEntity.removeHighLight();
                        }

                        IS_ENTITY_SELECTED = false;
                        SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;

                        target = null;
                        entityID = -1;
                        InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;
                        InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;
                        selectedEntity = null;
                        System.out.println("deselect");
                    }

                }
            }
        }
    }

    /*
    to check the basics game states seperate from the interaction states
     */
    public void updateGameState() {

        if (main.client != null) {
            IS_CLIENT_CONNECTED = true;
        } else {
            IS_CLIENT_CONNECTED = false;
        }

        IS_RUNNING = main.isRunning();

        hudInput();
    }

    public void hudInput() {

        if (HUD.IS_CREATE_ENTITY_BUTTON_PRESSED) {
            Action.sendCreateEntity(main.sendNetworkMessage);
            HUD.IS_CREATE_ENTITY_BUTTON_PRESSED = false;
        }

    }
}
