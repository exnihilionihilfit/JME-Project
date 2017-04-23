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
            position = new Vector3f((float) (Math.random() * mapSize), 10 - (float) (Math.random() * 20), (float) (Math.random() * mapSize));

            entityContainer = new EntityContainer(-1L, Entities.getNewEntityId(), "asteroid", "asteroid", position);

            while (SimpleCollision.checkCollision(entityContainer, Entities.ENTITY_CONTAINER)) {
                entityContainer.position = new Vector3f((float) (Math.random() * mapSize), 10 - (float) (Math.random() * 20), (float) (Math.random() * mapSize));
            }

            if (entityContainer != null) {
                Entities.addEntity(entityContainer);
            }
        }

    }

}
