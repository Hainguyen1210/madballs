/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.GameObject;
import madballs.collision.*;
import madballs.map.SpawnLocation;
import madballs.wearables.Weapon;


/**
 *
 * @author haing
 */
public class WeaponItem extends Item{
  private Weapon weapon;
  
  public WeaponItem(Environment environment, Class<Weapon> weaponClass, SpawnLocation spawnLocation) {
    super(environment, spawnLocation);
      try {
          weapon = weaponClass.getDeclaredConstructor(GameObject.class).newInstance(this);
      } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
          Logger.getLogger(WeaponItem.class.getName()).log(Level.SEVERE, null, ex);
      }
    weapon.setCollisionPassiveBehaviour(new Ball_n_WallBehaviour(new DisappearBehaviour(new ReleaseSpawnLocation(null, getSpawnLocation()))));
    weapon.setCollisionEffect(new GiveWeaponEffect(null, weapon));
      setCollisionEffect(new GiveWeaponEffect(null, weapon));
  }
 

  @Override
  public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setColor(Paint.valueOf("blue"));
    setSize(1);
    setHitBox(new Circle(getSize(), getColor()));
  }
  
}
