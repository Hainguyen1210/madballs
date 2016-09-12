package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * the interface with the method that can be called to check if a collision can happen
 * Created by caval on 03/09/2016.
 */
public interface Callback {
    boolean checkCondition(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape);
}
