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
public class FireRate extends StackedCollisionEffect{
    private double fireRateAmount;
    private int effectTimeout;

    public double getFireRateAmount() {
        return fireRateAmount;
    }

    public int getEffectTimeout() {
        return effectTimeout;
    }
    
    public FireRate(StackedCollisionEffect effect, double fireRateAmount, int effectTimeout) {
        super(effect);
        this.fireRateAmount = fireRateAmount;
        this.effectTimeout = effectTimeout;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        System.out.println("madballs.Collision.Boost.FireRate.affect()");
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
}
