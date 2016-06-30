/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class StackedCollisionPassiveBehaviour implements CollisionPassiveBehaviour{
    private CollisionPassiveBehaviour wrappedBehaviour;

    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (wrappedBehaviour != null) wrappedBehaviour.getAffected(source, target, effect, collisionShape);
    }
    
    public StackedCollisionPassiveBehaviour(CollisionPassiveBehaviour behaviour) {
        wrappedBehaviour = behaviour;
    }
    
    /**
     * the recursive method to check whether this combo behaviour contains a specific behaviour
     * @param c
     * @return 
     */
    public boolean hasCollisionBehaviour(Class c){
        if (c.isInstance(this)) return true;
        if (wrappedBehaviour != null && wrappedBehaviour instanceof StackedCollisionPassiveBehaviour){
            return ((StackedCollisionPassiveBehaviour) wrappedBehaviour).hasCollisionBehaviour(c);
        }
        else {
            return false;
        }
    }
    
}
