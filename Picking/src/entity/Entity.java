/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import com.jme3.math.Vector3f;



/**
 *
 * @author chasma
 */
public class Entity {
    
    private Vector3f position;
    private float healthPoints;
    
    public void setPosition(Vector3f position)
    {
        this.position = position;
    }
    
    public Vector3f getPositon()
    {
        return this.position;
    }
    
    public void setHealthPoints(float healthPoints)
    {
        this.healthPoints = healthPoints;
    }
    
    public float getHealthPoints()
    {
        return this.healthPoints;
    }
    
}
