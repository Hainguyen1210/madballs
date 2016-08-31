package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.moveBehaviour.StraightMove;
import madballs.projectiles.Projectile;

/**
 * Created by caval on 31/08/2016.
 */
public class DeflectiveBehaviour extends StackedCollisionPassiveBehaviour {
    public DeflectiveBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (source.getMoveBehaviour() instanceof StraightMove){
            StraightMove straightMove = (StraightMove) source.getMoveBehaviour();
            straightMove.setVelocityX(-straightMove.getVelocityX());
            straightMove.setVelocityY(-straightMove.getVelocityY());
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return source instanceof Projectile;
    }
}
