package madballs.wearables;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.collision.*;

/**
 * Created by caval on 31/08/2016.
 */
public class Shield extends Weapon {
    private final double WIDTH = 7;
    private final double HEIGHT = 40;

    public Shield(GameObject owner, Integer id) {
        super(owner, 0, 0, id);

        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new VulnerableBehaviour(new DeflectiveBehaviour(new PushableBehaviour(null))));

        setDamage(0);
        ammoProperty().bind(getHp());
        setFireRate(0.1);
        setRange(0);
        setProjectileSpeed(0);
        setProjectileHitBoxSize(0.1);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

//        setFireSoundFX("pistol");
        setProjectileCollisionEffect(new NullEffect(null));
    }

    @Override
    public void forceFire(Integer id){

    }

    @Override
    public void setDisplayComponents() {
        setWidth(7);
        setHeight(20);
        setHitBox(new Rectangle(WIDTH, HEIGHT, Color.LIGHTBLUE));
        getHitBox().setTranslateX(getOwner().getHitBox().getBoundsInLocal().getWidth()/2);
        getHitBox().setTranslateY(-HEIGHT/2);
    }
}
