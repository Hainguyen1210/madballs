package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * Created by caval on 18/08/2016.
 */
public class DisappearWithOwnerBehaviour extends DisappearBehaviour {

    public DisappearWithOwnerBehaviour(StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.dieWithOwner();
    }
}
