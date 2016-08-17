/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;

import madballs.multiplayer.BuffData;

/**
 *
 * @author chim-
 */
public class Frenzy extends BuffState{
    private double fireRateRatio;

    public Frenzy(BuffData data){
        super(data);
    }

    public Frenzy(BuffState effectState, int duration, double fireRateRatio) {
        super(effectState, duration);
        this.fireRateRatio = fireRateRatio;
    }

    @Override
    public void fade() {
        getBall().getWeapon().setFireRate(getBall().getWeapon().getFireRate() / fireRateRatio);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }

    @Override
    public double[] getParameters() {
        return new double[] {fireRateRatio};
    }

    @Override
    public void recreateFromData(BuffData data) {
        fireRateRatio = data.getParameters()[0];
    }

    @Override
    public void apply() {
        getBall().getWeapon().setFireRate(getBall().getWeapon().getFireRate() * fireRateRatio);
    }
}
