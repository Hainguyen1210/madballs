/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import madballs.collision.Ball_n_WallBehaviour;
import madballs.collision.DisappearBehaviour;
import madballs.collision.MakeUpItem;
import madballs.Environment;
import madballs.GameObject;
import madballs.collision.ReleaseSpawnLocation;
import madballs.map.SpawnLocation;

/**
 *
 * @author chim-
 */
public abstract class Item extends GameObject{
  private ImageView image;
  private SpawnLocation lastSpawnLocation;
  private int size;
  private Paint color;

  public ImageView getImage() {
    return image;
  }

  public int getSize() {
    return size;
  }

  public Paint getColor() {
    return color;
  }

  public void setColor(Paint color) {
    this.color = color;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public SpawnLocation getLastSpawnLocation() {
    return lastSpawnLocation;
  }

  public void setLastSpawnLocation(SpawnLocation lastSpawnLocation) {
    this.lastSpawnLocation = lastSpawnLocation;
  }

  public Item(Environment environment, SpawnLocation spawnLocation) {
      super(environment, spawnLocation.getX(), spawnLocation.getY(), true);
    setLastSpawnLocation(spawnLocation);
//      setDisplay();
//      setCollisionEffect(new NullEffect(null));
      setCollisionPassiveBehaviour(new Ball_n_WallBehaviour(new DisappearBehaviour(new ReleaseSpawnLocation(new MakeUpItem(null)))));
  }
  
  public boolean canSpawn(){
      return false;
  }

  @Override
  public void updateUnique(long now) {

  }

  private void setImage(ImageView image) {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
    
}