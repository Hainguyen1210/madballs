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
  private Weapon weapon;
  
  public Weapon getWeapon(){
    return weapon;
  }
  
  public GiveWeaponEffect(StackedCollisionEffect effect, Weapon weapon) {
    super(effect);
    this.weapon = weapon;
  }
  
  @Override
  public void affect(GameObject source, GameObject target, Shape collisionShape) {
    
        System.out.println("111111111");
      target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
      super.affect(source, target, collisionShape);
  }

}
