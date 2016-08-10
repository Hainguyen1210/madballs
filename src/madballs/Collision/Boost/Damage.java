/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision.Boost;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.collision.StackedCollisionEffect;
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
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
}
