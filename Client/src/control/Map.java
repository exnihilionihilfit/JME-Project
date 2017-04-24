/**
 * This is the map and all components are set and stored
 */
package control;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
/**
 *
 * @author chasma
 */
public class Map {

    private Geometry floor;

    /**
     * To create a target-point for movement
     *
     * @param assetManager
     * @return 
     */
    public  Geometry makeFloor(AssetManager assetManager) {
        Box box = new Box(1500, .02f, 1500);
        floor = new Geometry("Floor", box);
        floor.setLocalTranslation(0, -5, 0);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Sky.j3md");

        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setTransparent(true);

        floor.setMaterial(mat);
        return floor;
    }
    
       public void initSky(AssetManager assetManager,Node rootNode) {
        Texture westTex = assetManager.loadTexture("Textures/background/west.png");
        Texture eastTex = assetManager.loadTexture("Textures/background/east.png");
        Texture northTex = assetManager.loadTexture("Textures/background/north.png");
        Texture southTex = assetManager.loadTexture("Textures/background/south.png");
        Texture upTex = assetManager.loadTexture("Textures/background/up.png");
        Texture downTex = assetManager.loadTexture("Textures/background/down.png");
        final Vector3f normalScale = new Vector3f(1, 1, 1);

        Spatial skySpatial = SkyFactory.createSky(assetManager, westTex, eastTex, southTex, northTex, upTex, downTex, normalScale);

        rootNode.attachChild(skySpatial);
    }

}
