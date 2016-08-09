/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;

/**
 *
 * @author haing
 */
public class SpeedBoost extends Item{
  public SpeedBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
    super(environment, x, y, isSettingDisplay);
    
  }

  @Override
  public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setColor(Paint.valueOf("yellow"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
  }
  
}
