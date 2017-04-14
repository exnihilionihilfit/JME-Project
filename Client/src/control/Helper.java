/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import main.Main;
import model.Entity;

/**
 *
 * @author chasma
 */
public class Helper {

    public static Entity getEntityByID(int entityID) {
        for (Entity entity : Main.getEntities()) {
            if (entity.getID() == entityID) {
                return entity;

            }
        }
        return null;
    }

}
