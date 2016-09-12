package madballs.collision;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Shape;
import madballs.Explosion;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.multiplayer.SpawnData;
import madballs.projectiles.Projectile;

/**
 * creates an Explosion when colliding with a PushBack obj
 * Created by caval on 16/08/2016.
 */
public class ExplosiveBehaviour extends StackedCollisionPassiveBehaviour {
    private double radius, duration;
    private DoubleProperty damage = new SimpleDoubleProperty();
    private Integer ballID;

    public ExplosiveBehaviour(double radius, double damage, double duration, Integer ballID, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.radius = radius;
        this.damage.set(damage);
        this.ballID = ballID;
        this.duration = duration;
    }

    public ExplosiveBehaviour(double radius, DoubleProperty damageProperty, double duration, Integer ballID, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.radius = radius;
        this.damage.bind(damageProperty);
        this.ballID = ballID;
        this.duration = duration;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (MadBalls.isHost()){
            Explosion explosion = new Explosion(
                    target.getEnvironment(), target.getTranslateX(), target.getTranslateY(),
                    radius, damage.get(), duration,
                    -1, ballID, ((Projectile)target).getSourceWeapon().getID());
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof PushBackEffect && !target.isDead();
    }
}
