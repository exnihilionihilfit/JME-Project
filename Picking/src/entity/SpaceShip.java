/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entityInterfaces.Moveable;

/**
 *
 * @author chasma
 */
public class SpaceShip implements Moveable{

    private float movementSpeed;
    private float rotationSpeed;

    @Override
    public void setMovementSpeed(float movementSpeed) {
      this.movementSpeed = movementSpeed;
    }

    @Override
    public double getMovementSpeed() {
       return this.movementSpeed;
    }

    @Override
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public double getRotationSpeed() {
       return this.rotationSpeed; 
    }
    
    

    
}
