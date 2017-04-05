/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.jme3.math.Vector3f;

/**
 *
 * @author chasma
 */
public class MyWayList {
    
    Vector3f newLocation = null;
    
    public Vector3f getNewLocation()
    {
        return newLocation;
    }
    public void setNewLocation(Vector3f newLocation)
    {
        this.newLocation = newLocation;
    }
    
}
