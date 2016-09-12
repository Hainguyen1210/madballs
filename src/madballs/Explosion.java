package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.collision.DamageEffect;
import madballs.collision.InvulnerableBehaviour;
import madballs.gameFX.SoundStudio;
import madballs.multiplayer.SpawnData;

/**
 * A GameObject that deals damage in a circular area
 * Created by caval on 16/08/2016.
 */
public class Explosion extends GameObject {
    private double radius;
    private long explodedTime;
    private double duration;
    private Integer weaponID;

    public Integer getWeaponID() {
        return weaponID;
    }

    public Explosion(Environment environment, double x, double y, double radius, double damage, double duration, Integer id, Integer ballID, Integer weaponID) {
        super(environment, x, y, false, id);
        SoundStudio.getInstance().playAudio("explosion", x, y, 800, 800);
        explodedTime = getEnvironment().getLastUpdateTime();
        this.radius = radius;
        this.duration = duration;
        this.weaponID = weaponID;
        setDisplay(id);
        setCollisionEffect(new DamageEffect(damage, 1.1, ballID, null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));

        if (MadBalls.isHost()) {
            MadBalls.getMultiplayerHandler().sendData(new SpawnData("explosion", new double[]{x, y, radius, damage, duration}, getID()));
        }
    }

    @Override
    public void setDisplayComponents() {
        Circle circle = new Circle(radius);
        circle.setFill(Paint.valueOf("orange"));
        setHitBox(circle);
        setImage("explosion");
        configImageView(-radius, -radius, radius*2, radius*2);
    }

    @Override
    public void updateUnique(long now) {
        if ((now - explodedTime) / 1000000000 > duration){
            die();
        }
    }
}
