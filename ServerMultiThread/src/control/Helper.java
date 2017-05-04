/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.math.Vector3f;
import java.util.List;
import model.Player;

/**
 *
 * @author novo
 */
public class Helper {

    public static Vector3f convertStringToVector3f(String nextPosition) {

        String replace = nextPosition.replace("(", "");
        replace = replace.replace(")", "");
        replace = replace.trim();
        String[] values = replace.split(",");

        Vector3f vector = new Vector3f();
        if (values.length == 3) {
            vector = new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
        }

        return vector;

    }

    public static String[] prepareEntitiesList(List<Player> playerList) {

        return new String[20];
    }
}
