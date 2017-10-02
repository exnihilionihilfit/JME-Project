/**
 * All updates e.g. positon or health will be set here
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
            entity.updateState(tpf);
        });
    }

}
