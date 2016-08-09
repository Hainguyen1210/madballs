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
public class Heal extends StackedCollisionEffect{
    private int healAmount;
    private int effectTimeout;

    public int getHealAmount() {
        return healAmount;
    }

    public int getEffectTimeout() {
        return effectTimeout;
    }
    
    public Heal(StackedCollisionEffect effect, int healAmount, int effectTimeout) {
        super(effect);
        this.healAmount = healAmount;
        this.effectTimeout = effectTimeout;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        System.out.println("madballs.Collision.Boost.Heal.affect()");
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        super.affect(source, target, collisionShape);
    }
}
