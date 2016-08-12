/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;
/**
 *
 * @author chim-
 */
public class FireRateBuff extends BuffState{
    private double fireRate;

    public FireRateBuff(BuffState effectState, int duration, double fireRate) {
        super(effectState, duration);
        this.fireRate = fireRate;
    }

    @Override
    public void fade() {
        getBall().getWeapon().setFireRate(getBall().getWeapon().getFireRate() / fireRate);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }

    @Override
    public void apply() {
        getBall().getWeapon().setFireRate(getBall().getWeapon().getFireRate() * fireRate);
    }
}
