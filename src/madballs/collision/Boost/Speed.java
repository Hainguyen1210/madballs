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
public class Speed extends StackedCollisionEffect{
    private int speedAmount;
    private int effectTimeout;

    public double getEffectTimeout() {
        return effectTimeout;
    }

    public int getSpeedAmount() {
        return speedAmount;
    }
    
    public Speed(StackedCollisionEffect effect, int speedAmount, int effectTimeout) {
        super(effect);
        this.speedAmount = speedAmount;
        this.effectTimeout = effectTimeout;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
    
}
