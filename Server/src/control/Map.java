/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import control.pathfinding.Pathfinding;
import java.awt.Rectangle;
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
    private static final Pathfinding path = new Pathfinding(new Vector3f(0, 0, 0),new Vector3f(1000,0,1000), 10);;

    public Map(float mapSize, int numberOfAsteroids) {
        this.mapSize = mapSize;
        this.numberOfAsteroids = numberOfAsteroids;
    // init pathfinding with expansion of node-net
       
	path.setMap();
       
        generateAstroids();

    }
    
    public static Pathfinding getPathfinding()
    {
        return Map.path;
    }

    private void generateAstroids() {
        Vector3f position = null;

        EntityContainer entityContainer = null;

        for (int i = 0; i < this.numberOfAsteroids; i++) {
            position = new Vector3f((float) (Math.random() * mapSize)+100, 0, (float) (Math.random() * mapSize));

            entityContainer = new EntityContainer(-1L, Entities.getNewEntityId(), "Asteroid_" + Entities.getNewEntityId(), EntityTypes.ASTEROID, position);
            entityContainer.setDirection( new Vector3f((float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI)));

            while (SimpleCollision.checkCollision(entityContainer, Entities.ENTITY_CONTAINER)) {
                entityContainer.setPosition(new Vector3f((float) (Math.random() * mapSize), 0, (float) (Math.random() * mapSize)));

                entityContainer.setDirection(new Vector3f((float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI), (float) (Math.random() * 2 * Math.PI)));

            }
            
            
            entityContainer.setCollisionTypes(CollisionTypes.NONMOVEABLE);
            entityContainer.setSize(20);

            if (entityContainer != null) {
                this.path.setBlockedNodes(new Rectangle((int)entityContainer.getPosition().x -15, (int)entityContainer.getPosition().z -15, 30, 30));
                
                Entities.addEntity(entityContainer);
            }
        }
        
      
    }

}
