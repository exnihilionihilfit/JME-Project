/**
 * This is the map and all components are set and stored
 */
package control;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
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
     * @param assetManager
     */
    public Geometry makeFloor(AssetManager assetManager) {
        Box box = new Box(1500, .02f, 1500);
        floor = new Geometry("Floor", box);
        floor.setLocalTranslation(0, -5, 0);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Sky.j3md");

        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setTransparent(true);

        floor.setMaterial(mat);
        return floor;
    }

}
