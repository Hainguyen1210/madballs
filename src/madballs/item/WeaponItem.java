/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.collision.GiveWeaponEffect;
import madballs.wearables.Awp;
import madballs.wearables.Pistol;
import madballs.wearables.Weapon;


/**
 *
 * @author haing
 */
public class WeaponItem extends Item{
  private Weapon weapon;
  
  public WeaponItem(Environment environment, double x, double y, boolean isSettingDisplay, String weaponClassStr) {
    super(environment, x, y, isSettingDisplay);
    if(weaponClassStr.equals("madballs.Wearables.Awp")){
      weapon = new Awp(this);
    } else if (weaponClassStr.equals("madballs.Wearables.Pistol")){
      weapon = new Pistol(this);
    }
      setCollisionEffect(new GiveWeaponEffect(null, weapon));
  }
 

  @Override
  public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setColor(Paint.valueOf("blue"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
  }
  
}
