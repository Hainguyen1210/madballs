/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.collision.GiveStateEffect;
import madballs.effectState.BuffState;

/**
 *
 * @author haing
 */
public class BuffItem extends Item{
  protected GiveStateEffect giveStateEffect;
  
  public BuffItem(Environment environment, double x, double y, BuffState effectState) {
    super(environment, x, y);
    giveStateEffect = new GiveStateEffect(null, effectState);
    setCollisionEffect(giveStateEffect);
  }  
  
  @Override
  public void setDisplayComponents() {
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
  }
}
