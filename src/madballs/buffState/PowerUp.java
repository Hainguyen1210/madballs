/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;
import madballs.collision.DamageEffect;
import madballs.multiplayer.BuffData;

/**
 *
 * @author chim-
 */
public class PowerUp extends BuffState{
    private double damageRatio;

    public PowerUp(BuffData data){
        super(data);
    }

    public PowerUp(BuffState effectState, int duration, double damageRatio) {
        super(effectState, duration);
        this.damageRatio = damageRatio;
    }
    
    @Override
    public void fade() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() / damageRatio);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }

    @Override
    public double[] getParameters() {
        return new double[]{damageRatio};
    }

    @Override
    public void recreateFromData(BuffData data) {
        damageRatio = data.getParameters()[0];
    }

    @Override
    public void apply() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() * damageRatio);
        getBall().getWeapon().setProjectileCollisionEffect(new DamageEffect(null, getBall().getWeapon().getDamage()));
    }
}
