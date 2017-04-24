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

   
//The MyWayList object that contains the result waylist:
    private MyWayList wayList = null;
//The future that is used to check the execution status:
    private Future future = null;
    private long lastTime;

    private Main mainApp = null;

    private String name = null;
    private final AssetManager assetManager;
    private Node entityNode;

    

    private boolean recevedNewPositionMessage = false;
    private boolean reseavedNewPositionMessage = false;
    private final int entityID;
    private boolean isMoveable = true;
    private Node entity;
    
    private boolean isSelected = false;
    private  long playerId;
    private Vector3f direction = new Vector3f(1, 0, 1);
    private final String type;

    public Entity(Main mainApp, AssetManager assetManager, String name, String type, int entityId,long playerId) {
        this.mainApp = mainApp;
        this.name = name;
        this.assetManager = assetManager;
        this.entityID = entityId;
        this.playerId = playerId;
        this.type = type;

    }
    
    public void setPlayerId(long playerId)
    {
        this.playerId = playerId;
    }
    
    public long getPlayerId()
    {
        return this.playerId;
    }

    public void addHighlight() {
        getEntityNode().addLight(Main.entityHighLightLight);
    }

    public void removeHighLight() {
        getEntityNode().removeLight(Main.entityHighLightLight);
         getEntityNode().removeLight(Main.entityNeutralHighLightLight);
    }
      public void addNeutralHighlight() {
        getEntityNode().addLight(Main.entityNeutralHighLightLight);
    }
    
    public boolean isSelected(){
        return this.isSelected;
       
    }
    public void setSelected(boolean value)
    {
        this.isSelected = value;
    }

    public boolean isMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean value) {
        this.isMoveable = value;
    }

    public String getName() {
        return this.name;
    }

    public void sendNewPositionMessage(boolean b) {
        this.reseavedNewPositionMessage = b;
    }

    public boolean isRecevedNewPositionMessage() {
        return this.recevedNewPositionMessage;
    }

    public void setRecevedNewPositionMessage(boolean b) {
        this.recevedNewPositionMessage = b;
    }

  

    public boolean isSendNewPositionMessage() {
        return this.reseavedNewPositionMessage;
    }

    public void setSendNewPositionMessage(boolean b) {
        this.reseavedNewPositionMessage = b;
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
      
    }

    private Vector3f getLocalTranslation() {
        return entityNode.getLocalTranslation();
    }

    public Node getEntityNode() {

        if (entity == null) {
            
            if(this.type.equals("ship"))
            {
            // load a character from jme3test-test-data
            entity = (Node) assetManager.loadModel("Models/shuttle_final/shuttle_final.j3o");
            entity.setName("entity");
            entity.setUserData("id", entityID);
            entity.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
            }
            else if(this.type.equals("asteroid"))
            {
              entity = (Node) assetManager.loadModel("Models/asteroid/asteroid.j3o");
            
             
               entity.setName("entity");
              entity.setUserData("id", entityID);
              entity.scale((float) (Math.random() * 12f));
             
            }
       
        }
        
     
        return entity;

    }

    public boolean getPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setToLookAt(Vector3f targetPosition) {
     
        if(targetPosition != null)
        {
        getEntityNode().lookAt(targetPosition, Vector3f.UNIT_Y);
        }
        
    }
    
    public Vector3f getDirection()
    {
        return this.direction;
    }

    public void setPosition(Vector3f position) {
        getEntityNode().setLocalTranslation(position);
    }

    public void setDirection(Vector3f newDirection) {
        this.direction = newDirection;
        // to make sure 
        newDirection = newDirection.mult(5f).add(getEntityNode().getLocalTranslation());
        getEntityNode().lookAt(newDirection, Vector3f.UNIT_Y);
    }

  

}
