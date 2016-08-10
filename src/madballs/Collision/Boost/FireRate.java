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
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
}
