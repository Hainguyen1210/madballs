/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
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
        if (source instanceof Obstacle) {
          Spawner spawner = new Spawner(source.getEnvironment());
          spawner.randomSpawn();
        }
    }
}
