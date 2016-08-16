/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.multiplayer.SpawnData;
import madballs.wearables.Ak47;
import madballs.wearables.Awp;
import madballs.wearables.M4A1;
import madballs.wearables.Minigun;
import madballs.wearables.Pistol;
import madballs.wearables.Uzi;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class Spawner {
  private Class<Weapon>[] weapons;
  private Class<Item>[] boostItems;
  private Random random = new Random();
  private Environment environment;
  private LongProperty lastItemSpawnTime = new SimpleLongProperty(0);
//  private ArrayList<SpawnLocation> itemSpawnLocations;
  
  
  public Spawner(Environment environment){
    this.environment = environment;
    weapons = new Class[] {Awp.class,Uzi.class, Ak47.class, Minigun.class, M4A1.class, Pistol.class};
//      boostItems = new Class[] {Wheels.class};
    boostItems = new Class[] {MiniHealthFlask.class, DivinePotion.class, FullPotion.class, SpicyBiscuit.class, PlasmaAmmo.class, Wheels.class};
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
      spawnItem(spawnLocation.getX(), spawnLocation.getY(), -1);
    }
  }
  
  public void spawnWeapon(double X, double Y, int weaponIndex){
      if (weaponIndex < 0){
          weaponIndex = random.nextInt(weapons.length);
      }
      Class<Weapon> weaponClass = weapons[weaponIndex];
      if (MadBalls.isHost()){
          MadBalls.getMultiplayerHandler().sendData(new SpawnData(X, Y, "weapon", weaponIndex));
      }
//    System.out.println("asfasdfasf" + weaponType.getName());
        new WeaponItem(environment, X, Y, weaponClass);
    
    
      System.out.println(weaponClass);
  }
  public void spawnItem(double X, double Y, int itemIndex){
      if (itemIndex < 0){
          itemIndex = random.nextInt(boostItems.length);
      }
      Class<Item> itemClass = boostItems[itemIndex];
      if (MadBalls.isHost()){
        MadBalls.getMultiplayerHandler().sendData(new SpawnData(X, Y, "item", itemIndex));
      }
        try {
        itemClass.getDeclaredConstructor(Environment.class, double.class, double.class).newInstance(environment, X, Y);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Spawner.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
}
