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
import control.CreateEntityGeometry;
import control.finateStateMachine.StackFSM;
import control.finateStateMachine.State;
import control.finateStateMachine.StateEntity;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import main.Main;

/**
 * Data a redudant all basic data should be set in
 * entityContainer which is used for server exchange
 * all other local data needed should be here only !
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
    
    private Node entityGeometry;

    private long playerId;

    private final EntityTypes type;
    private EntityContainer entityContainer;
    private final StackFSM mapFiniteStateMachine;
 
    private boolean isSelected;
  

    public Entity(Main mainApp, AssetManager assetManager, EntityContainer entityContainer) {
        this.mainApp = mainApp;
        this.name =  entityContainer.getName();
        this.assetManager = assetManager;
        this.entityID =  entityContainer.getEntityId();
        this.playerId =  entityContainer.getPlayerId();
        this.type =  entityContainer.getType();
        this.entityContainer = entityContainer;
        
        mapFiniteStateMachine = new StackFSM(this,mainApp);        
        mapFiniteStateMachine.pushState(StateEntity.HOLD);  
        
 
    }
    
    public EntityContainer getEntityContainer()
    {
        return entityContainer;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return this.playerId;
    }
    
    public boolean isMoveable() {
        return this.entityContainer.isIsMoveable();
    }

    public void setMoveable(boolean value) {
       this.entityContainer.setIsMoveable(value);
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

    public void updateState(float tpf) {      
        mapFiniteStateMachine.update(tpf);
      
    }

    private Vector3f getLocalTranslation() {
        return entityNode.getLocalTranslation();
    }

    
    
    public Node getEntityNode()
    {
         if (entityGeometry == null && this.type != null) {
            entityGeometry =  CreateEntityGeometry.getEntityNode(type, entityID, assetManager);
         }
         
         return entityGeometry;
    }

    public boolean getPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setToLookAt(Vector3f targetPosition) {

        if (targetPosition != null) {
            getEntityNode().lookAt(targetPosition, Vector3f.UNIT_Y);
        }

    }

    public Vector3f getDirection() {
        return this.entityContainer.getDirection();
    }

    public void setPosition(Vector3f position) {
        Node tmp  = getEntityNode();
        if(tmp != null)
        {
              getEntityNode().setLocalTranslation(position);
        }
      
    }

    public void setDirection(Vector3f newDirection) {
        this.entityContainer.setDirection(newDirection);
        // to make sure 
        newDirection = newDirection.mult(5f).add(getEntityNode().getLocalTranslation());
        getEntityNode().lookAt(newDirection, Vector3f.UNIT_Y);
    }

    public EntityTypes getType() {
        return this.type;
    }

    public void addHighlight() {
        if(this.playerId != Player.getPlayerId())
        {
            getEntityNode().addLight(Main.highLightNeutral);
        }
        else
        {
            getEntityNode().addLight(Main.highLightOwnEnitity);  
        }
    }
    
    
    public void removeHighLight() {
        getEntityNode().removeLight(Main.highLightOwnEnitity);
        getEntityNode().removeLight(Main.highLightNeutral);
    }



    public void setSelected(boolean b) {
        this.isSelected = b;
        
        if(b)
        {
            addHighlight();
        }
        else
        {
            removeHighLight();
        }
    }

    public void updateFromServer(EntityContainer entityContainer) {
        
                setDirection(entityContainer.getDirection());
                setPosition(entityContainer.getPosition());
                           
                
                 this.entityContainer = entityContainer;
    }
    
    
    public void changeState(State state) {        
        mapFiniteStateMachine.changeState(state);
    }
    
    public State getState()
    {
        return mapFiniteStateMachine.getCurrentState();
    }
    

  



}
