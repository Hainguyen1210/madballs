package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.map.SpawnLocation;

/**
 * the behaviour created specially for Items when they are picked up, so that new Items can be spawned at their locations
 * Created by hainguyen on 8/17/16.
 */
public class ReleaseSpawnLocation extends StackedCollisionPassiveBehaviour {
    private SpawnLocation spawnLocation;

    public ReleaseSpawnLocation(SpawnLocation spawnLocation, StackedCollisionPassiveBehaviour behaviour) {
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
