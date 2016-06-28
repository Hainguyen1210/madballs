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
public class ComboCollisionPassiveBehaviour implements CollisionPassiveBehaviour{
    private CollisionPassiveBehaviour wrappedBehaviour;

    @Override
    public void getAffected(GameObject source, GameObject target, ComboCollisionEffect effect) {
        if (wrappedBehaviour != null) wrappedBehaviour.getAffected(source, target, effect);
    }
    
    public ComboCollisionPassiveBehaviour(CollisionPassiveBehaviour behaviour) {
        wrappedBehaviour = behaviour;
    }
    
    /**
     * the recursive method to check whether this combo behaviour contains a specific behaviour
     * @param c
     * @return 
     */
    public boolean isInstanceOf(Class c){
        if (c.isInstance(this)) return true;
        if (wrappedBehaviour != null && wrappedBehaviour instanceof ComboCollisionPassiveBehaviour){
            return ((ComboCollisionPassiveBehaviour) wrappedBehaviour).isInstanceOf(c);
        }
        else {
            return false;
        }
    }
    
}
