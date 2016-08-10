/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.util.Random;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.Item.DamageBoost;
import madballs.map.Map;
import madballs.wearables.Awp;
import madballs.wearables.Pistol;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class Spawner {
  private Class<Weapon>[] weapons;
  private Random random = new Random();
  private Environment environment;
  private LongProperty lastItemSpawnTime = new SimpleLongProperty(0);
  
  
  public Spawner(Environment environment){
    this.environment = environment;
    weapons = new Class[] {Awp.class, Pistol.class};
  }
  
  public void spawn(long now){
    if((now - lastItemSpawnTime.get()) / 1000000000.0 > 5){
      lastItemSpawnTime.set(now);
      randomSpawn();
    }
  }
  public void randomSpawn(){
    Map map = environment.getMap();
    int X = random.nextInt((int) map.getLENGTH());
    int Y = random.nextInt((int) map.getHEIGHT());
    int itemOrWeapon = random.nextInt(2);
    if(itemOrWeapon == 0){
      System.out.println("Weapon spawned");
//      spawnWeapon(X, Y);
    } else {
      System.out.println("Item spawned");
      spawnItem(X, Y);
    }
  }
  // ------- spawn weapon ------- 
//  public void spawnWeapon(int X, int Y){
//    Class<Weapon> weaponType = weapons[random.nextInt(weapons.length)];
//    
//    Weapon weapon = null;
////    System.out.println("asfasdfasf" + weaponType.getName());
//    WeaponItem weaponItem = new WeaponItem(environment, X, Y, false, weaponType.getName());
//    
//    
//      System.out.println(weaponType);
//  }
  public void spawnItem(int X, int Y){
//    Item item = new SpeedBoost(environment, X, Y, false);
//    Item item = new HealBoost(environment, X, Y, false);
//    Item item = new FireRateBoost(environment, X, Y, false);
//    Item item = new DamageBoost(environment, X, Y, false);
    Item item = new DamageBoost(environment, X, Y, false);
  }
}
