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

/**
 *
 * @author chim-
 */
public abstract class Item extends GameObject{
  private ImageView image;
  private int size;
  private Paint color;
  public boolean isSpawned = false;

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

  public Item(Environment environment, double x, double y, boolean isSettingDisplay) {
      super(environment, x, y, isSettingDisplay);
      setDisplay();
//      setCollisionEffect(new NullEffect(null));
      setCollisionPassiveBehaviour(new Ball_n_WallBehaviour(new DisappearBehaviour(new MakeUpItem(null))));
  }

  @Override
  public void updateUnique(long now) {

  }

  private void setImage(ImageView image) {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
    
}