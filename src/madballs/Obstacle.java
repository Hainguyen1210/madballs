/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.Collision.InvulnerableBehaviour;
import madballs.Collision.PushBackEffect;

/**
 *
 * @author Caval
 */
public class Obstacle extends GameObject{
    private double length;
    private double height;

    public Obstacle(Environment environment, double x, double y, double length, double height) {
        super(environment, x, y);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
        Rectangle rect = new Rectangle(49, 49, Paint.valueOf("black"));
        setHitBox(rect);
    }

    @Override
    public void update(long now) {
        
    }
    
}
