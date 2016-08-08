/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import java.util.Random;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.Map.Map;
import madballs.Wearables.Awp;
import madballs.Wearables.Pistol;
import madballs.Wearables.Weapon;

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
    if((now - lastItemSpawnTime.get()) / 1000000000.0 > 3){
      lastItemSpawnTime.set(now);
      randomSpawn();
    }
  }
  public void randomSpawn(){
    
      Map map = environment.getMap();
      int X = random.nextInt((int) map.getLENGTH());
      int Y = random.nextInt((int) map.getHEIGHT());
//      Item item = new SpeedBoost(environment, X, Y, false);
      
      System.out.println(X + " " + Y);
  }
}
