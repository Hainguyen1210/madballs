/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.Wearables.Weapon;

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
        System.out.println("111111111");
      if (effect.hasCollisionEffect(GiveWeaponEffect.class)) {
        Weapon weapon = ((GiveWeaponEffect)effect).getWeapon();
        if(weapon != null) ((Ball)target).setWeapon(weapon);
        System.out.println("Get weapon " + weapon);
      }
      super.getAffected(source, target, effect, collisionShape);
  }

}
