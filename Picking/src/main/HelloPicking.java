package main;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import static java.lang.Thread.State.TERMINATED;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HelloPicking extends SimpleApplication {

    AmbientLight ambiLight = new AmbientLight(ColorRGBA.Blue);
    Geometry hittedGeo = new Geometry();
    Vector3f camerPosition = new Vector3f(0, 100, 0);
    float cameraYZoomFactor = 100;

    public static void main(String[] args) {
        HelloPicking app = new HelloPicking();
        app.start();
    }
    private Node shootables;
    private Geometry mark;
    private Geometry floor;
    private BulletAppState bulletAppState;
    private Thread collision;

    @Override
    public void simpleUpdate(float tpf) {

        if (shootables != null && shootables.getChildren() != null && shootables.getChildren().size() > 0) {
            for (Spatial spatial : shootables.getChildren()) {

                if (collision == null || collision.getState().equals(TERMINATED)) {
                    collision = new Thread(new collisionDetection(shootables));
                    collision.start();

                }

                if (spatial.getUserData("isHit") != null) {
                    if (spatial.getUserData("hittedObject") != null) {
                        reactOnCollision(spatial);
                    }
                }

                //  System.out.println(spatial.getUserData("isHit")+" "+spatial.getUserData("hittedObject"));
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

            RigidBodyControl shipControll = ship.getControl(RigidBodyControl.class);

            if (shipControll != null) {
                shipControll.setPhysicsLocation(ship.getLocalTranslation());
                shipControll.setPhysicsRotation(ship.getLocalRotation());
            }

            System.out.println(" arrived ");
        } else {
            ship.setLocalTranslation(ship.getLocalTranslation().add(newPositionOnTheWay));
            ship.lookAt(destination, Vector3f.UNIT_Y);

            RigidBodyControl shipControll = ship.getControl(RigidBodyControl.class);

            if (shipControll != null) {
                shipControll.setPhysicsLocation(ship.getLocalTranslation());
                shipControll.setPhysicsRotation(ship.getLocalRotation());
            }

            // System.out.println(" flying to "+directionToTarget);
        }

        ship.setUserData("lastMoveUpdate", System.currentTimeMillis());

    }

    @Override
    public void simpleInitApp() {

        /**
         * Set up Physics
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));

        getCamera().lookAtDirection(new Vector3f(0, -1, 0), Vector3f.UNIT_Z);
        getCamera().setLocation(this.camerPosition);

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

        for (int i = 0; i < 0; i++) {
            shootables.attachChild(makeAsteroid("" + i));
        }

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

        inputManager.addMapping("Move", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "Move");

        inputManager.addMapping("MouseWheelForward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(analogListener, "MouseWheelForward");

        inputManager.addMapping("MouseWheelBackward", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(analogListener, "MouseWheelBackward");
    }

    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("MouseWheelForward")) {
                cameraYZoomFactor -= value;
                getCamera().setLocation(new Vector3f(0, cameraYZoomFactor, 0));
            }
            if (name.equals("MouseWheelBackward")) {
                cameraYZoomFactor += value;
                getCamera().setLocation(new Vector3f(0, cameraYZoomFactor, 0));
            }
        }

    };

    private final PhysicsCollisionListener collisionListener = new PhysicsCollisionListener() {
        @Override
        public void collision(PhysicsCollisionEvent event) {
            System.out.println("AHHHHHHH");
        }
    };

    /**
     * Defining the "Shoot" action: Determine what was hit and how to respond.
     */
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("MouseWheel") && !keyPressed) {
                System.out.println(keyPressed);
            }

            if (name.equals("Move") && !keyPressed) {
//                System.out.println("move!!");

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
                            // System.out.println(hittedGeo.getName());

                            Spatial ship = shootables.getChild(hittedGeo.getName());

                            ship.setUserData("destination", positionXY);
                            ship.setUserData("moveTo", true);
                            ship.setUserData("lastMoveUpdate", System.currentTimeMillis());

                            //  System.out.println(ship.getName() + " set new location ");
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
                    // System.out.println(results.getClosestCollision().getGeometry().getName());
                }

                // 4. Print the results
                //  System.out.println("----- Collisions? " + results.size() + "-----");
                for (int i = 0; i < results.size(); i++) {
                    // For each hit, we know distance, impact point, name of geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();

                    //  System.out.println("* Collision #" + i);
                    //  System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
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
    protected Spatial makeAsteroid(String name) {

        // load a character from jme3test-test-data
        Spatial ship = assetManager.loadModel("Models/asteroid/asteroid.j3o");
        ship.setName(name);
        ship.setName("asteroid");

        float xRandom = 20 + ((float) (Math.random() * 50)) - ((float) (Math.random() * 40));
        // float yRandom =((float) (Math.random() * 20)) - ((float) (Math.random() * 20));
        float zRandom = 20 + ((float) (Math.random() * 50)) - ((float) (Math.random() * 40));

        ship.scale((float) (Math.random() * 5));

        ship.setLocalTranslation(xRandom, 0.0f, zRandom);
        ship.rotate((float) ((float) -Math.random() * 2 * Math.PI), (float) ((float) -Math.random() * 2 * Math.PI), (float) ((float) -Math.random() * 2 * Math.PI));

        //rootNode.attachChild(ship);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(ship);
        RigidBodyControl shipBody = new RigidBodyControl(sceneShape, 0);

        shipBody.setKinematic(false);
        ship.addControl(shipBody);

        bulletAppState.getPhysicsSpace().add(ship);

        return ship;
    }

    /**
     * A floor to show that the "shot" can go through several objects.
     */
    protected Geometry makeFloor() {
        Box box = new Box(1500, .2f, 1500);
        floor = new Geometry("Floor", box);
        floor.setLocalTranslation(0, -5, 0);
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
        ship.setLocalTranslation(5, 0, 0);

        //rootNode.attachChild(ship);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(ship);
        RigidBodyControl shipBody = new RigidBodyControl(sceneShape, 0);

        shipBody.setKinematic(false);
        shipBody.setPhysicsLocation(new Vector3f(5, 0, 0));
        ship.addControl(shipBody);

        bulletAppState.getPhysicsSpace().add(ship);
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

        //rootNode.attachChild(ship);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(ship);
        RigidBodyControl shipBody = new RigidBodyControl(sceneShape, 0);

        shipBody.setKinematic(false);
        ship.addControl(shipBody);

        bulletAppState.getPhysicsSpace().add(ship);
        return ship;
    }

    private void reactOnCollision(Spatial spatial) {

        String hittedObjectName = (String) spatial.getUserData("hittedObject");

        Spatial hittedObject = shootables.getChild(hittedObjectName);

        hittedObject.setUserData("isHit", false);
        hittedObject.setUserData("hittedObject", null);

        Vector3f hittedObjectTranslation = hittedObject.getLocalTranslation();
        Vector3f spatialTranslation = spatial.getLocalTranslation();
        Vector3f distance = hittedObjectTranslation.subtract(spatialTranslation);
        Vector3f direction = distance.normalize();

        if (direction.equals(new Vector3f(0, 0, 0))) {
            direction = Vector3f.UNIT_X;
        }
        Vector3f tmp = direction.negate().multLocal(1.1f);

        Vector3f currentPosition = spatial.getLocalTranslation();
        Vector3f newPosition = currentPosition.add(tmp);

        // check result
        float newDistanceBetweenObjects = newPosition.subtract(hittedObjectTranslation).length();
        float oldDistanceBetweenObjects = distance.length();

        if (oldDistanceBetweenObjects < newDistanceBetweenObjects) {

            tmp = direction.multLocal(-1f);
            newPosition = currentPosition.add(tmp);

        }

        spatial.setLocalTranslation(newPosition);

        RigidBodyControl shipControll = spatial.getControl(RigidBodyControl.class);

        if (shipControll != null) {
            shipControll.setPhysicsLocation(newPosition);
        }

    }

    class collisionDetection implements Runnable {

        private final Node node;

        private collisionDetection(Node shootables) {
            node = shootables.clone(false);
        }

        private void checkCollision() {

            for (Spatial spatialA : node.getChildren()) {

                if (spatialA.getWorldBound() != null) {
                    CollisionResults results = new CollisionResults();

                    node.collideWith(spatialA.getWorldBound(), results);

                    for (int i = 0; i < results.size(); i++) {
                        // For each hit, we know distance, impact point, name of geometry.
                        if (!results.getCollision(i).getGeometry().getName().equals(spatialA.getName())) {
                            float dist = results.getCollision(i).getDistance();
                            Vector3f pt = results.getCollision(i).getContactPoint();
                            String hit = results.getCollision(i).getGeometry().getName();

                            Spatial hittedObject = shootables.getChild(hit);

                            hittedObject.setUserData("isHit", true);
                            hittedObject.setUserData("hittedObject", hit);

                            // System.out.println("* Collision #" + i);
                            //  System.out.println("  You " + spatialA.getName() + " shot " + hit + " at " + pt + ", " + dist + " wu away.");
                        }
                    }

                    results.clear();
                }

            }

        }

        @Override
        public void run() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(HelloPicking.class.getName()).log(Level.SEVERE, null, ex);
            }
            checkCollision();

        }
    }

}
