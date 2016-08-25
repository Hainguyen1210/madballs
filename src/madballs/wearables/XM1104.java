package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.ImageGenerator;
import madballs.MadBalls;
import madballs.collision.*;
import madballs.gameFX.SoundStudio;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.FireData;
import madballs.projectiles.Projectile;

import java.util.Random;

/**
 * Created by caval on 16/08/2016.
 */
public class XM1104 extends Weapon {
    private final double WIDTH = 30;
    private final double HEIGHT = 5;
    private final int varyingAngle = 40;
    private final Random random = new Random();

    public XM1104(GameObject owner, Integer id) {
        super(owner,
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);

        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setDamage(15);
        setAmmo(25);
        setFireRate(0.6);
        setRange(500);
        setProjectileSpeed(800);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

        setFireSoundFX("shotgun");

        setProjectileCollisionEffect(new DamageEffect(null, getDamage(), owner.getID()));
        setProjectileCollisionBehaviour(new WeaponIgnoredBehaviour(new DisappearBehaviour(null)));
    }

    @Override
    public void forceFire(Integer id){
        if (!MadBalls.isHost()) return;
        if (getFireSoundFX() != null) {
            SoundStudio.getInstance().playAudio(getFireSoundFX(), getTranslateX(), getTranslateY(), 600, 600);
        }
        for (int i = 0; i < 5; i++){
            Projectile projectile = new Projectile(this, new Circle(getProjectileHitBoxSize(), getProjectileColor()), getProjectileImageName(), id);
            double direction = Math.toRadians(getRotateAngle() + random.nextInt(varyingAngle / 2) * (random.nextBoolean() ? 1 : -1));
            ((StraightMove)projectile.getMoveBehaviour()).setNewDirection(direction);
            MadBalls.getMultiplayerHandler().sendData(new FireData(getID(), projectile.getID(), direction));
            if (checkAmmo()){
                return;
            }
        }
    }

    public void forceFire(Integer id, double direction){
        if (getFireSoundFX() != null) {
            SoundStudio.getInstance().playAudio(getFireSoundFX(), getTranslateX(), getTranslateY(), 600, 600);
        }
        Projectile projectile = new Projectile(this, new Circle(getProjectileHitBoxSize(), getProjectileColor()), getProjectileImageName(), id);
        ((StraightMove)projectile.getMoveBehaviour()).setNewDirection(direction);
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("grey")));
        setImage(ImageGenerator.getInstance().getImage("shotgun"));
        configImageView(0, -HEIGHT/2, HEIGHT, WIDTH);
    }


}
