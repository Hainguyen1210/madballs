/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision.Boost;

import javafx.scene.shape.Shape;
import madballs.Collision.StackedCollisionEffect;
import madballs.GameObject;
/**
 *
 * @author chim-
 */
public class Damage extends StackedCollisionEffect{
    private double damageAmount;
    private int effectTimeout;

    public double getDamageAmount() {
        return damageAmount;
    }

    public int getEffectTimeout() {
        return effectTimeout;
    }

    public Damage(StackedCollisionEffect effect, double damageAmount, int effectTimeout) {
        super(effect);
        this.damageAmount = damageAmount;
        this.effectTimeout = effectTimeout;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        System.out.println("madballs.Collision.Boost.Damage.affect()");
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
}
