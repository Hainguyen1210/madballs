package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.ImageGenerator;
import madballs.collision.*;

/**
 * Created by caval on 16/08/2016.
 */
public class Bazooka extends Weapon {
    private final double WIDTH = 60;
    private final double HEIGHT = 15;

    public Bazooka(GameObject owner, Integer id) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);

        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setScope(2.5);
        setDamage(200);
        setAmmo(2);
        setFireRate(0.25);
        setRange(2000);
        setProjectileSpeed(400);
        setProjectileHitBoxSize(20);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet4");

        setFireSoundFX("bazooka");
        setProjectileCollisionEffect(new NullEffect(null));
        setProjectileCollisionBehaviour(new ExplosiveBehaviour(new DisappearBehaviour(null), 100, getDamage(), owner.getID()));
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-30);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage(ImageGenerator.getInstance().getImage("bazooka"));
        configImageView(-30, -HEIGHT/2, HEIGHT, WIDTH);
    }
}
