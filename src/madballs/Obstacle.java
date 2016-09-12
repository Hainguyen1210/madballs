/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.InvulnerableBehaviour;
import madballs.collision.PushBackEffect;

/**
 *
 * @author Caval
 */
public class Obstacle extends GameObject{
    public double length;
    public double height;

    public Obstacle(Environment environment, double x, double y, double length, double height, Integer id) {
        super(environment, x, y, false, id);
        setMobile(false);
        this.length = length;
        this.height = height;
        setDisplay(id);
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
        setHitBox(new Rectangle(length, height, Paint.valueOf("green")));
        getHitBox().setOpacity(0);
    }

    @Override
    public void updateUnique(long now) {

    }

}
