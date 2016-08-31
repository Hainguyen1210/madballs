package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.Ground;
import madballs.MadBalls;
import madballs.collision.*;
import madballs.gameFX.SoundStudio;
import madballs.multiplayer.FireData;
import madballs.projectiles.Projectile;

/**
 * Created by caval on 31/08/2016.
 */
public class GrenadeLauncher extends Weapon {
    private final double WIDTH = 60;
    private final double HEIGHT = 15;

    public GrenadeLauncher(GameObject owner, Integer id) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);

        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(6);
        setScope(2);
        setDamage(60);
        setAmmo(5);
        setFireRate(1);
        setRange(700);
        setProjectileSpeed(400);
        setProjectileHitBoxSize(2);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

//        setFireSoundFX("bazooka");
        setProjectileCollisionEffect(new NullEffect(null));
        setProjectileCollisionBehaviour(new ObjExclusiveBehaviour(new ExplosiveBehaviour(new DisappearBehaviour(null), 30, getDamage(), owner.getID()), new Class[]{Ground.class}));
    }

    @Override
    public void forceFire(Integer projectileID){
        super.forceFire(projectileID);
        setRange(700);
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-30);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage("bazooka");
        configImageView(-30, -HEIGHT/2, HEIGHT, WIDTH);
    }
}
