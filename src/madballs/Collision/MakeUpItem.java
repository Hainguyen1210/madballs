/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.Item.Item;
import madballs.Item.Spawner;
import madballs.Obstacle;

/**
 *
 * @author haing
 */
public class MakeUpItem extends StackedCollisionPassiveBehaviour{
  
  public MakeUpItem(CollisionPassiveBehaviour behaviour) {
    super(behaviour);
  }
  
  @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
      Item item = null;
        if (source instanceof Obstacle){
          if(target instanceof Item){
            item = (Item) target;
            if(item.isSpawned == false){
              Spawner spawner = source.getEnvironment().getSpawner();
              spawner.randomSpawn();
              item.isSpawned = true;
            }
            
          }
        }
    }
}
