/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author chasma
 */
public class Map {
    
    private Geometry floor;
    

    
        /**
     * To create a target-point for movement
     * 
     */
    public Geometry makeFloor(AssetManager assetManager) {
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
    
}
