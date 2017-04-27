/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import model.Entities;
import model.EntityContainer;
import model.EntityTypes;

/**
 *
 * @author novo
 */
public class Map {

    private final float mapSize;
    private final int numberOfAsteroids;

    public Map(float mapSize, int numberOfAsteroids) {
        this.mapSize = mapSize;
        this.numberOfAsteroids = numberOfAsteroids;

        generateAstroids();

    }

    private void generateAstroids() {
        Vector3f position = null;

        EntityContainer entityContainer = null;

        for (int i = 0; i < this.numberOfAsteroids; i++) {
            position = new Vector3f((float) (Math.random() * mapSize), 0, (float) (Math.random() * mapSize));

            entityContainer = new EntityContainer(-1L, Entities.getNewEntityId(), "Asteroid_"+Entities.getNewEntityId(), EntityTypes.ASTEROID.name(), position);

            while (SimpleCollision.checkCollision(entityContainer, Entities.ENTITY_CONTAINER)) {
                entityContainer.position = new Vector3f((float) (Math.random() * mapSize), 0, (float) (Math.random() * mapSize));

                entityContainer.direction =   new Vector3f((float) (Math.random() * 2 * Math.PI),(float) (Math.random() * 2 * Math.PI),(float) (Math.random() * 2 * Math.PI));
             
            }
            
            

            if (entityContainer != null) {
                Entities.addEntity(entityContainer);
            }
        }

    }

}
