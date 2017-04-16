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
    
     public static ArrayList<EntityContainer> all = new ArrayList<>();

     public static int getNewEntityId()
     {
         entityId++;
         return entityId;
     }
}
