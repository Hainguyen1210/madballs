/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.util.ArrayList;
import java.util.Random;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.MadBalls;
import madballs.Item.DamageBoost;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.multiplayer.SpawnData;
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
//  private ArrayList<SpawnLocation> itemSpawnLocations;
  
  
  public Spawner(Environment environment){
    this.environment = environment;
    weapons = new Class[] {Awp.class, Pistol.class};
//    itemSpawnLocations = environment.getMap().getItemSpawnLocations();
  }
  
  public void spawn(long now){
    if((now - lastItemSpawnTime.get()) / 1000000000.0 > 5){
      lastItemSpawnTime.set(now);
      randomSpawn();
    }
  }
  public void randomSpawn(){
    Map map = environment.getMap();
//    int X = random.nextInt((int) map.getLENGTH());
//    int Y = random.nextInt((int) map.getHEIGHT());
    ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();
    SpawnLocation spawnLocation = itemSpawnLocations.get(random.nextInt(itemSpawnLocations.size()));
    int itemOrWeapon = random.nextInt(2);
    if(itemOrWeapon == 0){
      System.out.println("Weapon spawned");
      spawnWeapon(spawnLocation.getX(), spawnLocation.getY(), -1);
    } else {
      System.out.println("Item spawned");
      spawnItem(spawnLocation.getX(), spawnLocation.getY());
    }
  }
  
  public void spawnWeapon(double X, double Y, int weaponIndex){
      if (weaponIndex < 0){
          weaponIndex = random.nextInt(weapons.length);
      }
      Class<Weapon> weaponType = weapons[weaponIndex];
      if (MadBalls.isHost()){
          MadBalls.getMultiplayerHandler().sendData(new SpawnData(new SpawnLocation(X, Y, "weapon", weaponIndex)));
      }
    
    Weapon weapon = null;
//    System.out.println("asfasdfasf" + weaponType.getName());
    WeaponItem weaponItem = new WeaponItem(environment, X, Y, false, weaponType.getName());
    
    
      System.out.println(weaponType);
  }
  public void spawnItem(double X, double Y){
      if (MadBalls.isHost()){
//        MadBalls.getMultiplayerHandler().sendData(new SpawnData(new SpawnLocation(X, Y, "item", 1)));
      }
//    Item item = new SpeedBoost(environment, X, Y, false);
//    Item item = new HealBoost(environment, X, Y, false);
//    Item item = new FireRateBoost(environment, X, Y, false);
//    Item item = new DamageBoost(environment, X, Y, false);
    Item item = new DamageBoost(environment, X, Y, false);
  }
}
