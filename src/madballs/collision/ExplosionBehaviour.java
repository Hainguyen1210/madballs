package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Explosion;
import madballs.GameObject;

/**
 * Created by caval on 16/08/2016.
 */
public class ExplosionBehaviour extends StackedCollisionPassiveBehaviour {
    private double radius, damage;

    public ExplosionBehaviour(CollisionPassiveBehaviour behaviour, double radius, double damage) {
        super(behaviour);
        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        new Explosion(target.getEnvironment(), target.getTranslateX(), target.getTranslateY(), radius, damage);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof PushBackEffect;
    }
}
