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
    
    private int targetID;
    private int entityID;
    private Vector3f contatctPoint;
    private Vector3f pointOnFloor;
    
    public void setPointOnFloor(Vector3f pointOnFloor)
    {
        this.pointOnFloor = pointOnFloor;
    }
    
    
    public Vector3f getPointOnFloor()
    {
        return this.pointOnFloor;
    }
    
        public void setContactPoint(Vector3f contactPoint) {
         this.contatctPoint = contactPoint;
    }
    public Vector3f getContactPoint()
    {
        return this.contatctPoint;
    }
    
    public void setEntityID(int entityId){
        this.entityID = entityId;
    }
    
    public int getEntityID(){
        return this.entityID;
    }


    
}
