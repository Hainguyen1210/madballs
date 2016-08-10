/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.item.Item;
import madballs.item.Spawner;
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
            GameObject owner = target;
            while (owner != null){
                owner = owner.getOwner();
            }
          if(owner instanceof Item){
            item = (Item) owner;
            if(item.isSpawned == false){
              Spawner spawner = source.getEnvironment().getItemSpawner();
              spawner.randomSpawn();
              item.isSpawned = true;
            }
            owner.die();
            
          }
        }
        super.getAffected(source, target, effect, collisionShape);
    }
}
