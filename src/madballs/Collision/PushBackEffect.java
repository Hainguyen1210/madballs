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
public class PushBackEffect extends ComboCollisionEffect{

    public PushBackEffect(CollisionEffect effect) {
        super(effect);
    }

    @Override
    public void affect(GameObject source, GameObject target) {
        target.getCollisionPassiveBehaviour().getAffected(source, target, this);
        super.affect(source, target);
    }
    
}
