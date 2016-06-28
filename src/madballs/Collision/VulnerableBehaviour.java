/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class VulnerableBehaviour extends ComboCollisionPassiveBehaviour{

    public VulnerableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void getAffected(GameObject source, GameObject target, ComboCollisionEffect effect) {
        if (effect.isInstanceOf(DamageEffect.class)) {
            target.setHp(target.getHp() - ((DamageEffect)effect).getDamage());
        }
        super.getAffected(source, target, effect);
    }
    
}
