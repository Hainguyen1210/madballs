/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import madballs.Ball;
import madballs.GameObject;
import madballs.SceneManager;
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
        SceneManager.getInstance().displayLabel(effectState.getClass().getSimpleName(), source.getHitBox().getFill(), 2.5, target);
        effectState.castOn((Ball)target);
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect.hasCollisionEffect(GiveStateEffect.class);
    }
}
