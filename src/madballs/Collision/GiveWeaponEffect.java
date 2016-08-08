/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.Wearables.Weapon;

/**
 *
 * @author haing
 */
public class GiveWeaponEffect extends StackedCollisionEffect{
  private Class<Weapon> weaponClass;
  
  public Class<Weapon> getWeaponClass(){
    return weaponClass;
  }
  
  public GiveWeaponEffect(StackedCollisionEffect effect, Class<Weapon> weaponClass) {
    super(effect);
    this.weaponClass = weaponClass;
  }
  
  @Override
  public void affect(GameObject source, GameObject target, Shape collisionShape) {
      target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
      super.affect(source, target, collisionShape);
  }

}
