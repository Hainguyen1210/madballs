/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * only getAffected when the source is instance of certain Classes
 * @author chim-
 */
public class ObjExclusiveBehaviour extends StackedCollisionPassiveBehaviour{
    private Class[] exclusiveObjClasses;
    
    public ObjExclusiveBehaviour(Class[] exclusiveObjClasses, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.exclusiveObjClasses = exclusiveObjClasses;
    }
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        for (Class objClass : exclusiveObjClasses){
            if (objClass.isInstance(source)){
                super.getAffected(source, target, effect, collisionShape);
                return;
            }
        }
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {

    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return false;
    }
}
