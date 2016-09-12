package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.map.SpawnLocation;

/**
 * returns to the SpawnLocation
 * Created by caval on 03/09/2016.
 */
public class RecallBehaviour extends StackedCollisionPassiveBehaviour {
    private SpawnLocation spawnLocation;

    public RecallBehaviour(SpawnLocation spawnLocation, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.spawnLocation = spawnLocation;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.setTranslateX(spawnLocation.getX());
        target.setTranslateY(spawnLocation.getY());
        target.setOldX(spawnLocation.getX());
        target.setOldY(spawnLocation.getY());
        target.setRotate(0);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return MadBalls.isHost();
    }
}
