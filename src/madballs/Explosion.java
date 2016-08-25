package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.collision.DamageEffect;
import madballs.collision.InvulnerableBehaviour;
import madballs.gameFX.SoundStudio;

/**
 * Created by caval on 16/08/2016.
 */
public class Explosion extends GameObject {
    private double radius;
    private long explodedTime;

    public Explosion(Environment environment, double x, double y, double radius, double damage, Integer id, Integer ballID) {
        super(environment, x, y, false, id);
        SoundStudio.getInstance().playAudio("explosion", x, y, 800, 800);
        explodedTime = getEnvironment().getLastUpdateTime();
        this.radius = radius;
        setDisplay(id);
        setCollisionEffect(new DamageEffect(null, damage, ballID));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
        Circle circle = new Circle(radius);
        circle.setFill(Paint.valueOf("orange"));
        setHitBox(circle);
    }

    @Override
    public void updateUnique(long now) {
        if ((now - explodedTime) > 1000000000){
            die();
        }
    }
}
