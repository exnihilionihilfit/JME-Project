/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import model.EntityTypes;

/**
 *
 * @author novo
 */
public class CreateEntityGeometry {
    public static Node getEntityNode( EntityTypes type, int entityID, AssetManager assetManager) {

        Node entity = new Node();
        
        if ( type != null) {
            

            switch (type) {
                case BATTLESHIP:
                    entity = (Node) assetManager.loadModel("Models/shuttle_final/shuttle_final.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
                    break;
                case FREIGHTER:
                    entity = (Node) assetManager.loadModel("Models/freighter/freighter.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
                    break;
                case DRONE:
                    entity = (Node) assetManager.loadModel("Models/drone/drone.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
                    break;
                case ASTEROID:
                    entity = (Node) assetManager.loadModel("Models/asteroid/asteroid.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.scale(10);
                    break;
                case EXCHANGE_STATION:
                    entity = (Node) assetManager.loadModel("Models/building/building.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.scale(5);
                    break;
                case SENSOR_STATION:
                    entity = (Node) assetManager.loadModel("Models/building/building.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.scale(5);
                    break;
                case SKIFF:
                    entity = (Node) assetManager.loadModel("Models/building/building.j3o");
                    entity.setName("entity");
                    entity.setUserData("id", entityID);
                    entity.scale(5);
                    break;
                default:
                    break;
            }
         

        }
             
        
        return entity;

    }
}
