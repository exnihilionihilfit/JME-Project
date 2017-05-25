/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import model.EntityContainer;
import model.EntityProperties;
import model.EntityTypes;

/**
 *
 * @author novo
 */
public class PropertiesHandler {

   private static final ArrayList<EntityProperties> ENTITY_PROPERTIES = new ArrayList<>();

    /**
     * create properties for each entity type 
     * to set them on creation
     * should be loaded from file in
     * future
     */
    public static void load() {
   
        EntityProperties entityPropertie = new EntityProperties(EntityTypes.BATTLESHIP, true, false);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.FREIGHTER, true, false);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.DRONE, true, true);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.SKIFF, false, false);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.SENSOR_STATION, false, false);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.EXCHANGE_STATION, false, false);
        ENTITY_PROPERTIES.add(entityPropertie);

        entityPropertie = new EntityProperties(EntityTypes.ASTEROID, false, false);
        ENTITY_PROPERTIES.add(entityPropertie);
    }

    public static void setProperties(EntityContainer entityContainer) {
        
        for(EntityProperties entityPropertie:ENTITY_PROPERTIES)
        {
            if(entityPropertie.getEntityType().equals(entityContainer.type))
            {
                entityContainer.isBuildable = entityPropertie.isBuildable();
                entityContainer.isMoveable = entityPropertie.isMoveable();
            }
        }

    }

}
