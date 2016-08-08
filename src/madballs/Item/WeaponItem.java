/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import madballs.Collision.GiveWeaponEffect;
import madballs.Environment;
import madballs.Wearables.Weapon;

/**
 *
 * @author haing
 */
public class WeaponItem extends Item{
//  private Weapon weapon;
  

  public WeaponItem(Environment environment, double x, double y, boolean isSettingDisplay, Weapon weapon) {
    super(environment, x, y, true);
    setCollisionEffect(new GiveWeaponEffect(null, weapon));
//    this.weapon = weapon;
  }

  @Override
  public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
