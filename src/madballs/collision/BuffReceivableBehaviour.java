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
      if (effect.hasCollisionEffect(GiveStateEffect.class)) {
        GiveStateEffect receivedEffect = (GiveStateEffect)effect;
        BuffState effectState = receivedEffect.getEffectState();
        effectState.castOn((Ball)target);
      }
      
//      if (effect.hasCollisionEffect(Speed.class)){
//            int timeout = (int) ((Speed)effect).getEffectTimeout();
//            int speed = (int) ((Speed)effect).getSpeedAmount();
//            ((Ball)target).addEffectState(new SpeedBoosted(((Ball)target), null, timeout, speed));
//        } else if(effect.hasCollisionEffect(Heal.class)){
//            ((Ball)target).setHpValue(100);
//        } else if (effect.hasCollisionEffect(FireRate.class)) {
//            ((Ball)target).addEffectState(new FireRateBoosted(((Ball)target), null, 5, 1.5));
//        } else if (effect.hasCollisionEffect(Damage.class)) {
//            System.out.println("current dmg: " + ((Ball)target).getWeapon().getDamage());
//            ((Ball)target).addEffectState(new DamageBoosted(((Ball)target), null, 5, 2));
//            System.out.println("current dmg: " + ((Ball)target).getWeapon().getDamage());
//        }
    }
}
