package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.collision.*;
import madballs.gameFX.SoundStudio;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.FireData;
import madballs.projectiles.Projectile;

import java.util.Random;

/*
XM1104 is a shotgun which can shoot a wide-spread mutiple projectiles at a time
but only effective within short range.
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

        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(1.5);
        setDamage(22);
        setAmmo(30);
        setFireRate(1);
        setRange(400);
        setProjectileSpeed(800);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("red"));
        setProjectileImageName("bullet1");

        setFireSoundFX("shotgun");
    }

    @Override
    public Projectile forceFire(Integer id){
        if (!MadBalls.isHost()) return null;
        if (getFireSoundFX() != null) {
            SoundStudio.getInstance().playAudio(getFireSoundFX(), getTranslateX(), getTranslateY(), 150, 150);
        }
        for (int i = 0; i < 5; i++){
            generateProjectileCollisionType();
            Projectile projectile = new Projectile(this, getRange(), new Circle(getProjectileHitBoxSize(), getProjectileColor()), getProjectileImageName(), id);
            double direction = Math.toRadians(getRotateAngle() + random.nextInt(varyingAngle / 2) * (random.nextBoolean() ? 1 : -1));
            ((StraightMove)projectile.getMoveBehaviour()).setNewDirection(direction);
            MadBalls.getMultiplayerHandler().sendData(new FireData(getID(), projectile.getID(), direction));
            if (checkOutOfAmmo()){
                return null;
            }
        }
        return null;
    }

    public void forceFire(Integer id, double direction){
        generateProjectileCollisionType();
        if (getFireSoundFX() != null) {
            SoundStudio.getInstance().playAudio(getFireSoundFX(), getTranslateX(), getTranslateY(), 150, 150);
        }
        Projectile projectile = new Projectile(this, getRange(), new Circle(getProjectileHitBoxSize(), getProjectileColor()), getProjectileImageName(), id);
        ((StraightMove)projectile.getMoveBehaviour()).setNewDirection(direction);
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("grey")));
        setImage("shotgun");
        configImageView(0, -HEIGHT/2, HEIGHT, WIDTH);
    }


}
