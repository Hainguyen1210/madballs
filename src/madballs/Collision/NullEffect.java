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
public class NullEffect extends ComboCollisionEffect{
    
    public NullEffect(CollisionEffect effect) {
        super(effect);
    }
    
    @Override
    public void affect(GameObject source, GameObject target) {
        
    }
}
