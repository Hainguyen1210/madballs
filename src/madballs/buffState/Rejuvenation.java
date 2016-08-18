/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;

import javafx.scene.paint.Color;
import madballs.multiplayer.BuffData;

/**
 *
 * @author caval
 */
public class Rejuvenation extends BuffState{
    private double amount;

    public Rejuvenation(BuffData data){
        super(data);
    }

    public Rejuvenation(BuffState effectState, int duration, double amount) {
        super(effectState, duration);
        this.amount = amount;
    }
    @Override
    public void setColor() {
        setColor(Color.LIGHTGREEN);
    }

    @Override
    public double[] getParameters() {
        return new double[]{amount};
    }

    @Override
    public void recreateFromData(BuffData data) {
        amount = data.getParameters()[0];
    }

    @Override
    public void apply() {
    }

    @Override
    public void fade() {
        
    }

    @Override
    public void uniqueUpdate(long timestamp) {
        getBall().setHpValue(getBall().getHpValue() + amount/getDuration());
    }
}
