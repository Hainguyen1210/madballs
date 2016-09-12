package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * becomes immobile when collides
 * Created by caval on 01/09/2016.
 */
public class ImmobalizedBehaviour extends StackedCollisionPassiveBehaviour {
    public ImmobalizedBehaviour(StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.getMoveBehaviour().setSpeed(0);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return target.getMoveBehaviour() != null;
    }
}
