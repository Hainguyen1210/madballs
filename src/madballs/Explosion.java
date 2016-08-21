package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.collision.DamageEffect;
import madballs.collision.InvulnerableBehaviour;

/**
 * Created by caval on 16/08/2016.
 */
public class Explosion extends GameObject {
    private double radius;
    private long explodedTime;

    public Explosion(Environment environment, double x, double y, double radius, double damage) {
        super(environment, x, y, false);
        explodedTime = getEnvironment().getLastUpdateTime();
        this.radius = radius;
        setDisplay();
        setCollisionEffect(new DamageEffect(null, damage));
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
