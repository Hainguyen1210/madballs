/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;

/**
 *
 * @author haing
 */
public class GetWeaponEffect extends StackedCollisionPassiveBehaviour{
  
  public GetWeaponEffect(CollisionPassiveBehaviour behaviour) {
    super(behaviour);
  }
  
  
  @Override
  public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
//        System.out.println(source.getClass);
      if (effect.hasCollisionEffect(GiveWeaponEffect.class)) {
          ((Ball)target).setWeapon(((GiveWeaponEffect)effect).getWeaponClass());
      }
      super.getAffected(source, target, effect, collisionShape);
  }

}
