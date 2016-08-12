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
public class SpeedBuff extends BuffState{
    private double speed;
    
    public SpeedBuff(BuffState effectState, int duration, double speed) {
        super(effectState, duration);
        this.speed = speed;
    }


    @Override
    public void uniqueUpdate(long timestamp) {
        
    }

    @Override
    public void fade() {
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() - speed);
    }

    @Override
    public void apply() {
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() + speed);
    }
    
}
