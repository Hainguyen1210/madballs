package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.item.Item;
import madballs.map.SpawnLocation;
import madballs.wearables.Weapon;

/**
 * Created by hainguyen on 8/17/16.
 */
public class ReleaseSpawnLocation extends StackedCollisionPassiveBehaviour {
    private SpawnLocation spawnLocation;

    public ReleaseSpawnLocation(CollisionPassiveBehaviour behaviour, SpawnLocation spawnLocation) {
        super(behaviour);
        this.spawnLocation = spawnLocation;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (spawnLocation != null ) spawnLocation.setSpawned(false);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return true;
    }
}
