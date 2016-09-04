/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class ObjIgnoredBehaviour extends StackedCollisionPassiveBehaviour{
    private Class[] ignoredObjClasses;
    
    public ObjIgnoredBehaviour(Class[] ignoredObjClasses, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.ignoredObjClasses = ignoredObjClasses;
    }
    
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        for (Class objClass: ignoredObjClasses){
            if (objClass.isInstance(source)){
                return;
            }
        }
        super.getAffected(source, target, effect, collisionShape);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return false;
    }

}
