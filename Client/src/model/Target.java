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
public class Target {
    
    private int entityID;
    private Vector3f pickPoint;
    
    public void setPickPoint(Vector3f pickPoint)
    {
        this.pickPoint = pickPoint;
    }
    
    public Vector3f getPickPoint()
    {
        return this.pickPoint;
    }
    
    public void setEntityID(int entityId){
        this.entityID = entityId;
    }
    
    public int getEntityID(){
        return this.entityID;
    }
}
