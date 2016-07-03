/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import madballs.Collision.InvulnerableBehaviour;
import madballs.Collision.PushBackEffect;

/**
 *
 * @author Caval
 */
public class Obstacle extends GameObject{
    public double length;
    public double height;

    public Obstacle(Environment environment, double x, double y, double length, double height) {
        super(environment, x, y, false);
        
        this.length = length;
        this.height = height;
        setDisplay();
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
//        Rectangle rect = new Rectangle(49, 49, Paint.valueOf("green"));
//        rect.setArcHeight(15);
//        rect.setArcWidth(15);
//        setHitBox(rect);
        setHitBox(new Circle(25, Paint.valueOf("green")));
    }

    @Override
    public void update(long now) {
        
    }
    
}
