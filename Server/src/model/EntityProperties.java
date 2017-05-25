/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author novo
 */
public class EntityProperties {
    
   private final EntityTypes entityType;
   private final boolean isMoveable;
   private final boolean isBuildable;
    
    public EntityProperties(EntityTypes entityType, boolean isMoveable, boolean isBuildable)
    {
        this.isBuildable = isBuildable;
        this.isMoveable  = isMoveable;
        this.entityType = entityType;
    }
    
    public EntityTypes getEntityType()
    {
        return entityType;
    }
    
    public boolean isMoveable()
    {
        return isMoveable;
    }
    
    public boolean isBuildable()
    {
        return isBuildable;
    }
    
}
