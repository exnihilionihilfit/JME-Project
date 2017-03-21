/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entityInterfaces;

import com.jme3.math.Vector3f;

/**
 *
 * @author chasma
 */
public interface Moveable {
    
    
    public void setMovementSpeed(float movementSpeed);
    public double getMovementSpeed();
    
    public void setRotationSpeed(float rotationSpeed);
    public double getRotationSpeed();
    
}
