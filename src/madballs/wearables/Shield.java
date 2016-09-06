package madballs.wearables;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.collision.*;
import madballs.multiplayer.GetWeaponData;
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
        setMaxHp(200);
        setHpValue(200);
        ammoProperty().bind(getHp());
        setFireRate(0.1);
        setRange(0);
        setProjectileSpeed(0);
        setProjectileHitBoxSize(0.1);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

        if (MadBalls.isHost()) {
            ammoProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if ((int)newValue <= 0 && owner instanceof Ball){
                        ((Ball)owner).setWeapon(Pistol.class, -1);
                        MadBalls.getMultiplayerHandler().sendData(new GetWeaponData(owner.getID(), Pistol.class.getName(), ((Ball)owner).getWeapon().getID()));
                    }
                }
            });
        }

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
        setImage("shield");
        configImageView(getHitBox().getTranslateX(), getHitBox().getTranslateY(), HEIGHT, WIDTH);
    }
}
