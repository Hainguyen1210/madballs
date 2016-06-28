/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import madballs.GameObject;

/**
 * a collision effect that deals damage to the target
 * @author Caval
 */
public class DamageEffect extends ComboCollisionEffect{
    private double damage;
    
    public DamageEffect(CollisionEffect effect, double amount){
        super(effect);
        this.damage = amount;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public void affect(GameObject source, GameObject target) {
        target.getCollisionPassiveBehaviour().getAffected(source, target, this);
        super.affect(source, target);
    }
    
}
