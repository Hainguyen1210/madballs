package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.item.Item;
import madballs.wearables.Weapon;

/**
 * Created by hainguyen on 8/17/16.
 */
public class ReleaseSpawnLocation extends StackedCollisionPassiveBehaviour {
    public ReleaseSpawnLocation(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (target instanceof Item){
            ((Item)target).getLastSpawnLocation().setSpawned(false);
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return true;
    }
}
