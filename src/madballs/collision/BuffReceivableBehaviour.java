/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.effectState.BuffState;

/**
 *
 * @author chim-
 */
public class BuffReceivableBehaviour extends StackedCollisionPassiveBehaviour{
    
    public BuffReceivableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        GiveStateEffect receivedEffect = (GiveStateEffect)effect;
        BuffState effectState = receivedEffect.getEffectState();
        effectState.castOn((Ball)target);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect.hasCollisionEffect(GiveStateEffect.class);
    }
}
