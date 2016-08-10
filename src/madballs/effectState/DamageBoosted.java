/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;
import madballs.Ball;
import madballs.collision.DamageEffect;

/**
 *
 * @author chim-
 */
public class DamageBoosted extends EffectState{
    private double damage;

    public DamageBoosted(Ball ball, EffectState effectState, int duration, double damage) {
        super(ball, effectState, duration);
        this.damage = damage;
        ball.getWeapon().setDamage(ball.getWeapon().getDamage() * damage);
        ball.getWeapon().setProjectileCollisionEffect(new DamageEffect(null, ball.getWeapon().getDamage()));
    }
    
    @Override
    public void fade() {
        getBall().getWeapon().setDamage(getBall().getWeapon().getDamage() / damage);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
    }
    
}
