/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

import madballs.Ball;

/**
 *
 * @author chim-
 */
public class SpeedBoosted extends EffectState{
    private double speed;
    
    public SpeedBoosted(Ball ball, EffectState effectState, int duration, double speed) {
        super(ball, effectState, duration);
        this.speed = speed;
        ball.getMoveBehaviour().setSpeed(ball.getMoveBehaviour().getSpeed() + speed);
    }


    @Override
    public void uniqueUpdate(long timestamp) {
        
    }

    @Override
    public void fade() {
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() - speed);
    }
    
}
