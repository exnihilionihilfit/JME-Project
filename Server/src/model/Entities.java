/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author novo
 */
public class Entities {

    private static int entityId = 0;

    public static final ArrayList<EntityContainer> ENTITY_CONTAINER = new ArrayList<>();

    public static int getNewEntityId() {
        entityId++;
        return entityId;
    }

    public static void addEntity(EntityContainer entityContainer) {
        Entities.ENTITY_CONTAINER.add(entityContainer);
    }

    public static EntityContainer getEntityById(int entityId) {

        synchronized (ENTITY_CONTAINER) {
            for (EntityContainer entity : ENTITY_CONTAINER) {
                if (entityId == entity.getEntityId()) {
                    return entity;
                }
            }
        }
        return null;
    }
}
