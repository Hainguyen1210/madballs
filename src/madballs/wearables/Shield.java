package madballs.wearables;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.collision.*;
import madballs.projectiles.Projectile;

/**
 * Created by caval on 31/08/2016.
 */
public class Shield extends Weapon {
    private final double WIDTH = 12;
    private final double HEIGHT = 40;

    public Shield(GameObject owner, Integer id) {
        super(owner, 0, 0, id);

        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new VulnerableBehaviour(new PushableBehaviour(new ObjExclusiveBehaviour(new Class[] {Projectile.class}, new DeflectiveBehaviour(true, null)))));

        setWeight(2.5);
        setDamage(0);
        setMaxHp(150);
        setHpValue(150);
        ammoProperty().bind(getHp());
        setFireRate(0.1);
        setRange(0);
        setProjectileSpeed(0);
        setProjectileHitBoxSize(0.1);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

        ammoProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                checkOutOfAmmo();
            }
        });

//        setFireSoundFX("pistol");
        setProjectileCollisionEffect(new NullEffect(null));
    }

    @Override
    public Projectile forceFire(Integer id){
        return null;
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
