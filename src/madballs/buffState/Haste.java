/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;

import javafx.scene.paint.Paint;
import madballs.gameFX.SoundStudio;
import madballs.multiplayer.BuffData;

/**
 *
 * @author chim-
 */
public class Haste extends BuffState{
    private double speed;

    public Haste(BuffData data){
        super(data);
    }
    
    public Haste(BuffState effectState, int duration, double speed) {
        super(effectState, duration);
        this.speed = speed;
    }

    @Override
    public void setColor() {
        setColor(Paint.valueOf("yellow"));
    }

    @Override
    public void uniqueUpdate(long timestamp) {
        
    }

    @Override
    public void fade() {
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() - speed);
    }

    @Override
    public double[] getParameters() {
        return new double[0];
    }

    @Override
    public void recreateFromData(BuffData data) {

    }

    @Override
    public void apply() {
        SoundStudio.getInstance().playAudio("speedUp");
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() + speed);
    }
}
