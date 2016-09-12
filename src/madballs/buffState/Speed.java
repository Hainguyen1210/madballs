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
 * increase speed
 * @author chim-
 */
public class Speed extends BuffState{
    private double speed;

    public Speed(BuffData data){
        super(data);
    }
    
    public Speed(BuffState effectState, int duration, double speed) {
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
        if (speed >= 0){
            setName("Haste");
        }
        else {
            setName("Heavy");
        }

        SoundStudio.getInstance().playAudio("speedUp", getBall().getTranslateX(), getBall().getTranslateY(), 100, 100);
        getBall().getMoveBehaviour().setSpeed(getBall().getMoveBehaviour().getSpeed() + speed);
    }
}
