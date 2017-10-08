/**
 * To decide in which situation the game is.
 * So we can pick an object and e.g. move it
 */
package control;

import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import static control.finateStateMachine.StateEntity.MOVE;
import static control.finateStateMachine.StateEntity.SHOW_MENU;
import main.Main;
import model.Entity;
import model.EntityTypes;
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

    private double zoomFactor = 4.0f;
    private Vector3f currentLocation;
    private Vector2f mousePosition2d;
    private float tollerance;
    private EntityTypes TO_BUILD_ENTITY_TYPE = EntityTypes.NOT_DEFINED;
    private boolean IS_BUILDING_SET;
    private Node buildingPlacementHull;
    private boolean MOVE_PLACEMENT_HULL;
    private Target placementTarget;

    public GameState(Main main, InputManager inputManager, Node rootNode, Camera cam) {
        GameState.main = main;
        GameState.inputManager = inputManager;
        GameState.cam = cam;
        GameState.rootNode = rootNode;
    }

    public void entityInteraction() {

        if (IS_CLIENT_CONNECTED) {

            if (IS_PAUSED) {
                // pause
            } else {

                // deselct
                if (IS_ENTITY_SELECTED) {

                    /**
                     * Deselection
                     */
                    if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                        reset();
                        System.out.println("deselect");
                    }
                }
                /**
                 * check start condition If the entityID > -1 an entity was
                 * found if so start EntityAction cancle with rightMouse
                 *
                 * Note: the id is a fild set in enty and binde to the node we
                 * got the geometry wich is a child of the entity node so we
                 * have to "getParent" as long as we finde the node with the id
                 * entry
                 */
                if (IS_ENTITY_SELECTED == false) {

                    if (InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED) {
                        entityID = Action.getEnityOnMouseClickPoint(inputManager, cam, rootNode);
                        selectedEntity = Helper.getEntityByID(entityID);

                        if (selectedEntity != null) {
                            InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;
                            IS_ENTITY_SELECTED = true;
                        
                            selectedEntity.setSelected(true);
                            
                        }                     
                    }               

                }

                if (IS_ENTITY_SELECTED) {

                    // if its players entity
                    if (selectedEntity.getPlayerId() == Player.getPlayerId()) {

                        if(selectedEntity.getEntityContainer().isIsBuildable())
                        {
                            selectedEntity.changeBuildState(SHOW_MENU);
                        }
               
                        if (InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED) {             
                            selectedEntity.changeState(MOVE);                            
                        }
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

        EntityTypes shipType = null;

        if (HUD.IS_CREATE_BATTLESHIP_BUTTON_PRESSED) {

            shipType = EntityTypes.BATTLESHIP;
            HUD.IS_CREATE_BATTLESHIP_BUTTON_PRESSED = false;

        } else if (HUD.IS_CREATE_DRONE_BUTTON_PRESSED) {

            shipType = EntityTypes.DRONE;
            HUD.IS_CREATE_DRONE_BUTTON_PRESSED = false;

        } else if (HUD.IS_CREATE_FREIGHTER_BUTTON_PRESSED) {
            shipType = EntityTypes.FREIGHTER;
            HUD.IS_CREATE_FREIGHTER_BUTTON_PRESSED = false;

        }

        if (shipType != null) {
            Action.sendCreateEntity(main.sendNetworkMessage, "USS" + shipType, shipType, new Vector3f());
        }

    }

    public void mouseZoom() {
        currentLocation = GameState.main.getCamera().getLocation();

        if (InputListener.IS_WHEEL_FORWARD) {
            currentLocation.y -= zoomFactor;
        }
        if (InputListener.IS_WHEEL_BACKWARD) {
            currentLocation.y += zoomFactor;
        }

        if (InputListener.IS_WHEEL_BACKWARD || InputListener.IS_WHEEL_FORWARD) {
            GameState.main.getCamera().setLocation(currentLocation);
        }
    }

    public void moveCamera() {

        if (InputListener.IS_RESET_CAMERA_PRESSED) {
            GameState.main.getCamera().setLocation(GameState.main.CAMERA_START_POSITION);
        }

        tollerance = 30.0f;
        mousePosition2d = GameState.inputManager.getCursorPosition();

        // move camera left
        if (mousePosition2d.x <= 0 + tollerance) {
            currentLocation.x -= -1.0f;
            GameState.main.getCamera().setLocation(currentLocation);
        }
        // move camera right
        if (mousePosition2d.x >= GameState.main.screenWidth - tollerance) {
            currentLocation.x -= +1.0f;
            GameState.main.getCamera().setLocation(currentLocation);
        }
        // move camera forwad
        if (mousePosition2d.y >= GameState.main.screenHeight - tollerance) {
            currentLocation.z -= -1.0f;
            GameState.main.getCamera().setLocation(currentLocation);
        }
        // move camera backword
        if (mousePosition2d.y <= 0 + tollerance) {
            currentLocation.z -= +1.0f;
            GameState.main.getCamera().setLocation(currentLocation);
        }

    }

 

    private void reset() {
        selectedEntity.setSelected(false);
        //selectedEntity.changeState(HOLD);
        InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;
        //InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;

        IS_ENTITY_SELECTED = false;
        SEND_ENTITY_MOVE_ACTION_TO_SERVER = false;

        target = null;
        entityID = -1;
        // InputListener.IS_RIGHT_MOUSE_BUTTON_PRESSED = false;
        // InputListener.IS_LEFT_MOUSE_BUTTON_PRESSED = false;
        selectedEntity = null;
        // for building structures
        HUD.IS_BUILDABLE = false;
        HUD.IS_BUILDABLE(false);
        IS_BUILDING_SET = false;

        if (buildingPlacementHull != null) {
            main.getRootNode().detachChild(buildingPlacementHull);
        }
        MOVE_PLACEMENT_HULL = false;
        buildingPlacementHull = null;
        TO_BUILD_ENTITY_TYPE = EntityTypes.NOT_DEFINED;
    }
}
