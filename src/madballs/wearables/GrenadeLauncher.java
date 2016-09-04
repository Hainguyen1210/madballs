package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.Ground;
import madballs.collision.*;

/**
 * Created by caval on 31/08/2016.
 */
public class GrenadeLauncher extends Weapon {
    private final double WIDTH = 40;
    private final double HEIGHT = 10;

    public GrenadeLauncher(GameObject owner, Integer id) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);

        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(6);
        setScope(2);
        setDamage(60);
        setAmmo(5);
        setFireRate(1);
        setRange(700);
        setMaxRange(700);
        setProjectileSpeed(400);
        setProjectileHitBoxSize(2);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("grenade_ammo");

        setFireSoundFX("bazooka");

    }

    @Override
    public void generateProjectileCollisionType(){
        setProjectileCollisionEffect(new NullEffect(null));
        setProjectileCollisionBehaviour(
                new ObjExclusiveBehaviour(new Class[]{Ground.class},
                        new ExplosiveBehaviour(30, damageProperty(), 0.5, getOwner().getID(),
                                new DisappearBehaviour(null))));
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-10);
        setHeight(HEIGHT/2-2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage("grenade_launcher");
        configImageView(-10, -HEIGHT/2, HEIGHT, WIDTH);
    }
}
