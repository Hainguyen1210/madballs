/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import madballs.collision.GiveWeaponEffect;
import madballs.Environment;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class WeaponItem extends Item{
//  private Weapon weapon;
  

  public WeaponItem(Environment environment, double x, double y, boolean isSettingDisplay, Class<Weapon> weaponClass) {
    super(environment, x, y, true);
    setCollisionEffect(new GiveWeaponEffect(null, weaponClass));
//    this.weapon = weapon;
  }

  @Override
  public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
