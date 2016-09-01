package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * Created by caval on 01/09/2016.
 */
public class InvisibleBehaviour extends StackedCollisionPassiveBehaviour {
    public InvisibleBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.getDisplay().setOpacity(0);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return true;
    }
}
