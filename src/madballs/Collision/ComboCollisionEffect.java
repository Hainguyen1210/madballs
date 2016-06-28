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
public class ComboCollisionEffect implements CollisionEffect{
    CollisionEffect wrappedEffect;

    
    public ComboCollisionEffect(CollisionEffect effect) {
        wrappedEffect = effect;
    }
    
    @Override
    public void affect(GameObject source, GameObject target) {
        if (wrappedEffect != null) wrappedEffect.affect(source, target);
    }
    
    /**
     * the recursive method to check whether this combo effect contains a specific effect
     * @param c
     * @return 
     */
    public boolean isInstanceOf(Class c){
        if (c.isInstance(this)) return true;
        if (wrappedEffect != null && wrappedEffect instanceof ComboCollisionEffect){
            return ((ComboCollisionEffect) wrappedEffect).isInstanceOf(c);
        }
        else {
            return false;
        }
    }
}
