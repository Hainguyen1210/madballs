/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;
import madballs.collision.DamageEffect;

/**
 *
 * @author chim-
 */
public class DamageBuff extends BuffState{
    private double damageRatio;

    public DamageBuff(BuffState effectState, int duration, double damageRatio) {
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
    public void apply() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() * damageRatio);
        getBall().getWeapon().setProjectileCollisionEffect(new DamageEffect(null, getBall().getWeapon().getDamage()));
    }
    
}
