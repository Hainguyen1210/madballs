/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import static jdk.nashorn.internal.objects.NativeRegExp.source;
import madballs.Ball;
import madballs.Collision.Boost.Damage;
import madballs.Collision.Boost.FireRate;
import madballs.Collision.Boost.Heal;
import madballs.Collision.Boost.Speed;
import madballs.GameObject;
import madballs.effectState.HealBoosted;
import madballs.effectState.SpeedBoosted;
import madballs.effectState.FireRateBoosted;
import madballs.effectState.DamageBoosted;

/**
 *
 * @author chim-
 */
public class BoostReceivableBehaviour extends StackedCollisionPassiveBehaviour{
    
    public BoostReceivableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (effect.hasCollisionEffect(Speed.class)){
            int timeout = (int) ((Speed)effect).getEffectTimeout();
            int speed = (int) ((Speed)effect).getSpeedAmount();
            ((Ball)target).addEffectState(new SpeedBoosted(((Ball)target), null, timeout, speed));
        } else if(effect.hasCollisionEffect(Heal.class)){
            ((Ball)target).setHpValue(100);
//            System.out.println("curent HP: " + ((Ball)target).getHpValue());
        } else if (effect.hasCollisionEffect(FireRate.class)) {
            ((Ball)target).addEffectState(new FireRateBoosted(((Ball)target), null, 5, 1.5));
        } else if (effect.hasCollisionEffect(Damage.class)) {
            System.out.println("current dmg: " + ((Ball)target).getWeapon().getDamage());
            ((Ball)target).addEffectState(new DamageBoosted(((Ball)target), null, 5, 2));
            
            System.out.println("current dmg: " + ((Ball)target).getWeapon().getDamage());

        }
     
        
        super.getAffected(source, target, effect, collisionShape);
    }
}
