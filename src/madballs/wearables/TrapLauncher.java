package madballs.wearables;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.*;
import madballs.collision.*;
import madballs.projectiles.Projectile;

/**
 * Created by caval on 01/09/2016.
 */
public class TrapLauncher extends Weapon {
    private final double WIDTH = 30;
    private final double HEIGHT = 15;

    public TrapLauncher(GameObject owner, Integer id) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);

        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(2);
        setScope(1);
        setDamage(50);
        setAmmo(4);
        setFireRate(1);
        setRange(15);
        setProjectileSpeed(400);
        setProjectileHitBoxSize(10);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("trap");

//        setFireSoundFX("bazooka");
    }

    @Override
    public void generateProjectileCollisionType(){
        setProjectileCollisionEffect(new NullEffect(null));
        setProjectileCollisionBehaviour(
                new VulnerableBehaviour(
                        new ObjExclusiveBehaviour(new Class[]{Ground.class, Ball.class, Obstacle.class},
                                new ImmobalizedBehaviour(
                                        new InvisibleBehaviour(
                                                new ObjExclusiveBehaviour(new Class[]{Ball.class, Obstacle.class},
                                                        new ExplosiveBehaviour(30, damageProperty(), 0.5, getOwner().getID(),
                                                                new DisappearBehaviour(null))))))));
    }

    @Override
    public Projectile forceFire(Integer id){
        generateProjectileCollisionType();
        Projectile projectile = super.forceFire(id);
        if (projectile != null){
            projectile.setHpValue(50);
            int ownerID = getOwner().getID();
            projectile.getHp().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (MadBalls.isHost() && (double)newValue <= 0){
                        new Explosion(projectile.getEnvironment(), projectile.getTranslateX(), projectile.getTranslateY(), 30, getDamage(), 0.5, -1, ownerID, getID());
                    }
                }
            });
        }
        return projectile;
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT - 10);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage("trap_launcher");
        configImageView(0, -HEIGHT/2, HEIGHT, WIDTH);
    }
}
