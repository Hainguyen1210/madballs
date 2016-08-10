/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class GetWeaponBehaviour extends StackedCollisionPassiveBehaviour{
  
  public GetWeaponBehaviour(CollisionPassiveBehaviour behaviour) {
    super(behaviour);
  }
  
  
  @Override
  public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
      if (effect.hasCollisionEffect(GiveWeaponEffect.class)) {
        Weapon weapon = ((GiveWeaponEffect)effect).getWeapon();
        if(weapon != null) ((Ball)target).setWeapon(weapon);
        System.out.println("Get weapon " + weapon);
        GameObject owner = source.getOwner();
        if (owner != null) {
            owner.die();
        }
      }
      super.getAffected(source, target, effect, collisionShape);
  }

}
