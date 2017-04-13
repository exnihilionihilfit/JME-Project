/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.List;
import model.Entity;

/**
 *
 * @author novo
 */
public class UpdateEntity {
    
  

    public static void update(List<Entity> entities, float tpf) {
          entities.forEach((entity) -> {
            entity.update(tpf);
        });
    }
    
    
    
}
