/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

import madballs.Ball;
import madballs.Environment;
import madballs.gameFX.SoundStudio;

/**
 *
 * @author chim-
 */
public class Haste extends BuffState{
    private double speed;
    
    public Haste(BuffState effectState, int duration, double speed) {
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
        SoundStudio.getInstance().playSound("speedUp", getBall().getEnvironment().getLastUpdateTime(), 0);
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() + speed);
    }
    
}
