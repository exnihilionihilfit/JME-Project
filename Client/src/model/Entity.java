/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import main.Main;

/**
 *
 * @author chasma
 */
public class Entity {
    //The vector to store the desired location in:

    private Vector3f desiredLocation = new Vector3f();
//The MyWayList object that contains the result waylist:
     private MyWayList wayList = null;
//The future that is used to check the execution status:
    private Future future = null;
    private long lastTime;

    private Main mainApp = null;
    private final Vector3f translation;
    private String name = null;
    private final AssetManager assetManager;
    private Node node;

    private Vector3f nextPosition;
    private boolean couldMove = false;
    private boolean recevedNewPositionMessage = false;
    private boolean reseavedNewPositionMessage = false;
    private static int entityID;
    private boolean isMoveable = true;
    private Node ship;

    public Entity(Main mainApp, AssetManager assetManager, String name) {
        this.mainApp = mainApp;
        this.translation = new Vector3f();
        this.name = name;
        this.assetManager = assetManager;

        Entity.entityID++;

    }
    
    public boolean isMoveable()
    {
        return isMoveable;
    }
    
    public void setMoveable(boolean value)
    {
        this.isMoveable = value;
    }

    public String getName() {
        return this.name;
    }
    
    public void sendNewPositionMessage(boolean b)
    {
        this.reseavedNewPositionMessage  = b;
    }
    
    public boolean isRecevedNewPositionMessage()
    {
        return this.recevedNewPositionMessage;
    }
    
    public void setRecevedNewPositionMessage(boolean b)
    {
        this.recevedNewPositionMessage = b;
    }
  
    
    public Vector3f getNextPosition()
    {
        return this.nextPosition;
    }
    
    public void setNextPosition(Vector3f nextPosition)
    {
        this.nextPosition = nextPosition;
    }
    
    public boolean isSendNewPositionMessage()
    {
        return this.reseavedNewPositionMessage;
    }
    
    public void setSendNewPositionMessage(boolean b)
    {
        this.reseavedNewPositionMessage = b;
    }

    public void setNextPosition(String nextPosition) {

        String replace = nextPosition.replace("(", "");
        replace = replace.replace(")", "");
        replace = replace.trim();
        String[] values = replace.split(",");

        Vector3f nextPositionAsVector = new Vector3f();
        if (values.length == 3) {
            nextPositionAsVector = new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
        }

        this.nextPosition = nextPositionAsVector;

    }

    public void rotateGeometry(final Geometry geo, final Quaternion rot) {
        mainApp.enqueue(new Callable<Spatial>() {
            public Spatial call() throws Exception {
                return geo.rotate(rot);
            }
        });
    }

    Vector3f getGoodNextLocation() {

        return new Vector3f();
    }

    public int getID() {
        return entityID;
    }

    public String getNextPositionAsString() {
        return nextPosition.toString();
    }

    private Vector3f convertPositionAsStringIntoVector(String positionAsString) {
        return null;
    }

    Callable<MyWayList> newPosition = new Callable<MyWayList>() {

        @Override
        public MyWayList call() throws Exception {

            //Read or write data from the scene graph -- via the execution queue:
            Vector3f location = mainApp.enqueue(new Callable<Vector3f>() {
                public Vector3f call() throws Exception {
                    //we clone the location so we can use the variable safely on our thread
                    return getLocalTranslation().clone();
                }
            }).get();

            // This world class allows safe access via synchronized methods
            //Data data = myWorld.getData();
            //... Now process data and find the way ...
            Thread.sleep(2);
           

            //wayList.setNewLocation(nextPosition);
            //  System.out.println("get new Location"+wayList.getNewLocation()+" "+newLocation);

            //  System.out.println(name + " " + translation);
            return wayList;
        }

    };

    public void update(float tpf) {
                
    /*
        if (reseavedNewPositionMessage) {
            try {
                
                if (future == null) {
                    //set the desired location vector, after that we should not modify it anymore
                    //because it's being accessed on the other thread!
                    desiredLocation.set(getGoodNextLocation());
                    //start the callable on the executor

                    future = mainApp.executor.submit(newPosition);    //  Thread starts!
                    // System.out.println(findWay);
                } //If we have started a callable already, we check the status
                else if (future != null) {
                   

                    if (future.isDone()) {
                        wayList = (MyWayList) future.get();
                        future = null;
                    } else if (future.isCancelled()) {
                        //Set future to null. Maybe we succeed next time...
                        future = null;
                    }

                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Exception" + e);
            }
            reseavedNewPositionMessage = false;
        }

        if (wayList != null) {

            if (wayList.getNewLocation() != null) {     //   System.out.println("set new Location"+wayList.getNewLocation());
                getEntity().setLocalTranslation(wayList.getNewLocation());
                wayList = null;

            }

        }
*/
        if (nextPosition != null) {
            getEntity().setLocalTranslation(nextPosition);

           // recevedNewPositionMessage = false;
        }
    }

    private Vector3f getLocalTranslation() {
        return node.getLocalTranslation();
    }

    public Node getEntity() {
       
        if(ship == null)
        {
            // load a character from jme3test-test-data
        ship = (Node) assetManager.loadModel("Models/battleship5/battleship5.j3o");
        ship.setName("ship");
        ship.setUserData("id", Entity.entityID);          
        
        

        //rootNode.attachChild(ship);
       // CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(ship);
       // RigidBodyControl shipBody = new RigidBodyControl(sceneShape, 0);

        }
        return  ship;

    }

}
