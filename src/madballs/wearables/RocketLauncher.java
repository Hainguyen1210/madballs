package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.collision.*;

/**
 * Created by caval on 16/08/2016.
 */
public class RocketLauncher extends Weapon {
    private final double WIDTH = 60;
    private final double HEIGHT = 15;

    public RocketLauncher(GameObject owner) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25);

        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setDamage(200);
        setAmmo(5);
        setFireRate(0.25);
        setRange(2000);
        setProjectileSpeed(400);
        setProjectileHitBoxSize(20);
        setProjectileColor(Paint.valueOf("red"));

//        setFireSoundFX("pistol");
        setProjectileCollisionEffect(new NullEffect(null));
        setProjectileCollisionBehaviour(new ExplosionBehaviour(new DisappearBehaviour(null), 100, getDamage()));
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
    }
}
