package main;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * Sample 8 - how to let the user pick (select) objects in the scene using the
 * mouse or key presses. Can be used for shooting, opening doors, etc.
 */
public class NewClass extends SimpleApplication {

    AmbientLight ambiLight = new AmbientLight(ColorRGBA.Blue);
    Geometry hittedGeo = new Geometry();
    /**
     * Prepare the Physics Application State (jBullet)
     */
    private BulletAppState bulletAppState;
    RigidBodyControl floor_phy;
    RigidBodyControl asteroid_pyh;

    public static void main(String[] args) {
        HelloPicking app = new HelloPicking();
        app.start();
    }
    private Node shootables;
    private Geometry mark;
    private Geometry floor;

    @Override
    public void simpleUpdate(float tpf) {

        if (shootables != null && shootables.getChildren() != null && shootables.getChildren().size() > 0) {
            for (Spatial spatial : shootables.getChildren()) {
                
                CollisionResults results = new CollisionResults();
                spatial.collideWith(spatial, results);
                
                System.out.println(results);

                if (spatial.getUserData("destination") != null) {

                    Vector3f destination = spatial.getUserData("destination");

                    if (spatial.getUserData("speed") != null) {

                        float speed = spatial.getUserData("speed");

                        if (spatial.getUserData("lastMoveUpdate") != null) {

                            long lastMoveUpdate = spatial.getUserData("lastMoveUpdate");

                            if ((boolean) spatial.getUserData("moveTo") == true) {
                                moveObject(spatial);
                            }
                        }
                    }

                }
            }
        }

    }

    private void moveObject(Spatial ship) {

        // get node data
        Vector3f destination = ship.getUserData("destination");
        float speed = ship.getUserData("speed");
        long updateTime = ship.getUserData("lastMoveUpdate");

        // convert passed time since last update to seconds (speed is length per second)
        float timePassedInSeconds = ((float) (System.currentTimeMillis() - updateTime)) / 1000.0f;

        // rotate to new direction
        Vector3f distanceToTarget = destination.subtract(ship.getLocalTranslation());
        Vector3f directionToTarget = distanceToTarget.normalize();

        float distanceToTargetAsNumber = distanceToTarget.length();

        //calculate posible fly length this tick            
        float flyedLengthSinceLastUpdate = speed * timePassedInSeconds;

        Vector3f newPositionOnTheWay = directionToTarget.mult(flyedLengthSinceLastUpdate);

        // check if near and check if next step goes further then the target, because length is allways positiv
        if (0.01 > distanceToTargetAsNumber || distanceToTargetAsNumber - flyedLengthSinceLastUpdate < 0) {
            ship.setLocalTranslation(destination);
            ship.setUserData("moveTo", false);

            System.out.println(" arrived ");
        } else {
           
            ship.setLocalTranslation(ship.getLocalTranslation().add(newPositionOnTheWay));

            ship.lookAt(destination, Vector3f.UNIT_Y);

            System.out.println(" flying to " + directionToTarget);

        }

        ship.setUserData("lastMoveUpdate", System.currentTimeMillis());

    }

    @Override
    public void simpleInitApp() {
        /**
         * Set up Physics Game
         */
        bulletAppState = new BulletAppState();
      

        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
       
        bulletAppState.setDebugEnabled(true);
  
  
        getCamera().lookAtDirection(new Vector3f(0, -1, 0), Vector3f.UNIT_Z);
        getCamera().setLocation(new Vector3f(0, 30, 0));
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);

        // initCrossHairs(); // a "+" in the middle of the screen to help aiming
        initKeys();       // load custom key mappings
        initMark();       // a red sphere to mark the hit

        /**
         * create four colored boxes and a floor to shoot at:
         */
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        shootables.attachChild(makeAsteroid("a Dragon", -2f, 0f, 1f));
        shootables.attachChild(makeAsteroid("a tin can", 1f, -2f, 0f));
        shootables.attachChild(makeAsteroid("the Sheriff", 0f, 1f, -2f));
        shootables.attachChild(makeAsteroid("the Deputy", 1f, 0f, -4f));
        shootables.attachChild(makeFloor());
        shootables.attachChild(makeFreighter());
        shootables.attachChild(makeDrone());

        // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        AmbientLight am = new AmbientLight();
        rootNode.addLight(am);
    }

    /**
     * Declaring the "Shoot" action and mapping to its triggers.
     */
    private void initKeys() {
        inputManager.addMapping("Shoot",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, "Shoot");

        inputManager.addMapping("Move", new MouseButtonTrigger(mouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "Move");
    }

    /**
     * Defining the "Shoot" action: Determine what was hit and how to respond.
     */
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("Move") && !keyPressed) {
                System.out.println("move!!");

                // 1. Reset results list.        
                CollisionResults results = new CollisionResults();

                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtract(click3d).normalizeLocal();

                // 2. Aim the ray from cam loc to cam direction.
                Ray ray = new Ray(click3d, dir);

                if (floor != null) {
                    floor.collideWith(ray, results);
                }

                if (results.size() > 0) {

                    if (results.getClosestCollision().getGeometry().getName().equals("Floor")) {
                        CollisionResult closest = results.getClosestCollision();
                        // Let's interact - we mark the hit with a red dot.
                        Vector3f positionXY = closest.getContactPoint();
                        // set hight to 0
                        positionXY.setY(0f);

                        if (hittedGeo != null && hittedGeo.getParent() != null) {
                            System.out.println(hittedGeo.getName());

                            Spatial ship = shootables.getChild(hittedGeo.getName());

                            ship.setUserData("destination", positionXY);
                            ship.setUserData("moveTo", true);
                            ship.setUserData("lastMoveUpdate", System.currentTimeMillis());

                            System.out.println(ship.getName() + " set new location ");
                        }
                    }

                    // 5. Use the results (we mark the hit object)
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    // Let's interact - we mark the hit with a red dot.
                    mark.setLocalTranslation(closest.getContactPoint());
                    rootNode.attachChild(mark);
                } else {
                    // No hits? Then remove the red mark.
                    rootNode.detachChild(mark);
                }

            }

            if (name.equals("Shoot") && !keyPressed) {
                // 1. Reset results list.        
                CollisionResults results = new CollisionResults();

                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtract(click3d).normalizeLocal();

                // 2. Aim the ray from cam loc to cam direction.
                Ray ray = new Ray(click3d, dir);

                // 3. Collect intersections between Ray and Shootables in results list.
                // DO NOT check collision with the root node, or else ALL collisions will hit the skybox! Always make a separate node for objects you want to collide with.
                shootables.collideWith(ray, results);

                if (results.getClosestCollision() != null) {
                    hittedGeo.removeLight(ambiLight);

                    hittedGeo = results.getClosestCollision().getGeometry();

                    hittedGeo.addLight(ambiLight);
                    System.out.println(results.getClosestCollision().getGeometry().getName());
                }

                // 4. Print the results
                System.out.println("----- Collisions? " + results.size() + "-----");
                for (int i = 0; i < results.size(); i++) {
                    // For each hit, we know distance, impact point, name of geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();

                    System.out.println("* Collision #" + i);
                    System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                }
                // 5. Use the results (we mark the hit object)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    // Let's interact - we mark the hit with a red dot.
                    mark.setLocalTranslation(closest.getContactPoint());
                    rootNode.attachChild(mark);
                } else {
                    // No hits? Then remove the red mark.
                    rootNode.detachChild(mark);
                }
            }

        }
    };

    /**
     * A cube object for target practice
     */
    protected Geometry makeAsteroid(String name, float x, float y, float z) {
        Sphere sphere = new Sphere(50, 50, 2);
        Geometry cube = new Geometry(name, sphere);

        float xRandom = 1 + (float) (Math.random() * 8);
        float yRandom = (float) (Math.random() * 8);
        float zRandom = 1 + (float) (Math.random() * 8);

        cube.setLocalTranslation(xRandom, 0.0f, zRandom);
        cube.rotate((float) -Math.random(), (float) -Math.random(), (float) -Math.random());

        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        mat1.setTransparent(true);
        mat1.setTexture("ColorMap",
                assetManager.loadTexture("Textures/pole.jpg"));
        //mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        
                        asteroid_pyh = new RigidBodyControl(1.0f);
        cube.addControl(asteroid_pyh);
        bulletAppState.getPhysicsSpace().add(asteroid_pyh);
        
        return cube;
    }

    /**
     * A floor to show that the "shot" can go through several objects.
     */
    protected Geometry makeFloor() {
        Box box = new Box(1500, .2f, 1500);
        floor = new Geometry("Floor", box);
        floor.setLocalTranslation(0, 0, 0);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        mat1.setTransparent(true);
        mat1.setTexture("ColorMap", assetManager.loadTexture("Textures/transparent.png"));

        floor.setMaterial(mat1);
        return floor;
    }

    /**
     * A red ball that marks the last spot that was "hit" by the "shot".
     */
    protected void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    /**
     * A centred plus sign to help the player aim.
     */
    protected void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("[+]"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    protected Spatial makeFreighter() {

        // load a character from jme3test-test-data
        Spatial ship = assetManager.loadModel("Models/frachter/frachter.j3o");
        ship.setName("ship");
        ship.setUserData("destination", Vector3f.NAN);
        ship.setUserData("speed", 10f);
        ship.setUserData("moveTo", false);
        ship.setUserData("lastMoveUpdate", -1l);

        //ship.rotate(2.0f, 0.0f, 2.0f);
        ship.setLocalTranslation(0, 0, 0);

        // rootNode.attachChild(ship);
        floor_phy = new RigidBodyControl(1.0f);
        ship.addControl(floor_phy);
        floor_phy.setKinematic(true);
        bulletAppState.getPhysicsSpace().add(floor_phy);

        return ship;
    }

    protected Spatial makeDrone() {

        // load a character from jme3test-test-data
        Spatial ship = assetManager.loadModel("Models/drohne/drohne.j3o");
        ship.setName("drone");
        ship.setUserData("destination", Vector3f.NAN);
        ship.setUserData("speed", 5f);
        ship.setUserData("moveTo", false);
        ship.setUserData("lastMoveUpdate", -1l);

        //ship.rotate(2.0f, 0.0f, 2.0f);
        ship.setLocalTranslation(0, 0, 0);

        //  rootNode.attachChild(ship);
        floor_phy = new RigidBodyControl(1.0f);
        
        ship.addControl(floor_phy);
        floor_phy.setKinematic(true);
        bulletAppState.getPhysicsSpace().add(floor_phy);

       

        return ship;
    }
    
       public void processCollisions(Spatial shipA, Spatial shipB)
   {
       // COLLISION DETECTION - SPECIAL OBJECTS
 
       // check if two particular bounding volumes intersect. 
       if ( shipA.getWorldBound().intersects( shipB.getWorldBound() ) )
       {
           
       }
          
 
   }
}
