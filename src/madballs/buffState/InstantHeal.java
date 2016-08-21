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
public class InstantHeal extends InstantBuff{
    private double amount;

    public InstantHeal(BuffData data){
        super(data);
    }

    public InstantHeal(BuffState buffState, double amount) {
        super(buffState);
        this.amount = amount;
    }

    @Override
    public void setColor() {
        setColor(Color.GREEN);
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
        getBall().setHpValue(amount);
    }
}
