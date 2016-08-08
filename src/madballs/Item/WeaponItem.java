/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Ball;
import madballs.Collision.Ball_n_WallBehaviour;
import madballs.Collision.DisappearBehaviour;
import madballs.Collision.GiveWeaponEffect;
import madballs.Collision.MakeUpItem;
import madballs.Environment;
import madballs.Wearables.Weapon;

/**
 *
 * @author haing
 */
public class WeaponItem extends Item{
//  private Weapon weapon;
  

  public WeaponItem(Environment environment, double x, double y, boolean isSettingDisplay, Weapon weapon) {
    super(environment, x, y, isSettingDisplay);
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
