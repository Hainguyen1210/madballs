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
    private double damage;

    public DamageBuff(BuffState effectState, int duration, double damage) {
        super(effectState, duration);
        this.damage = damage;
    }
    
    @Override
    public void fade() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() / damage);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }

    @Override
    public void apply() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() * damage);
        getBall().getWeapon().setProjectileCollisionEffect(new DamageEffect(null, getBall().getWeapon().getDamage()));
    }
    
}
