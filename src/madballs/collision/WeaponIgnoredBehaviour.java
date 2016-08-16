/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.wearables.Weapon;

/**
 *
 * @author Caval
 */
public class WeaponIgnoredBehaviour extends StackedCollisionPassiveBehaviour{
    
    public WeaponIgnoredBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (source instanceof  Weapon) return;
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
