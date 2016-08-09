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
public class FireRateBoosted extends EffectState{
    private double fireRate;

    public FireRateBoosted(Ball ball, EffectState effectState, int duration, double fireRate) {
        super(ball, effectState, duration);
        this.fireRate = fireRate;
        ball.getWeapon().setFireRate(ball.getWeapon().getFireRate() * fireRate);
    }

    @Override
    public void fade() {
        getBall().getWeapon().setFireRate(getBall().getWeapon().getFireRate() / fireRate);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }
}
