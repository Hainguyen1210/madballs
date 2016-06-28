/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import madballs.Collision.PushBackEffect;
import madballs.Collision.VulnerableBehaviour;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Wearables.Weapon;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;

    public Ball(Environment environment, double a, double b) {
        super(environment, a , b);
        setMoveBehaviour(new StraightMove(this, 100));
        setCollisionEffect(new PushBackEffect(null));
        setCollisionPassiveBehaviour(new VulnerableBehaviour(null));
        
        setDisplay();
    }
    
    /**
     * set the hitBox and image of the Ball
     */
    private void setDisplay(){
        setHitBox(new Circle(25, Paint.valueOf("red")));
    }

    @Override
    public void update(long now) {
        getMoveBehaviour().move(now);
    }
}
