/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import java.util.ArrayList;
import java.util.Random;
import madballs.Environment;
import madballs.Map.Map;

/**
 *
 * @author haing
 */
public class Spawner {
  private Random random = new Random();
  private Environment environment;
  
  public Spawner(Environment environment){
    this.environment = environment;
  }
  public void randomSpawn(){
    Map map = environment.getMap();
    int X = random.nextInt((int) map.getLENGTH());
    int Y = random.nextInt((int) map.getHEIGHT());
    Item item = new SpeedBoost(environment, X, Y, false);
//    System.out.println(X + " " + Y);
  }
}
