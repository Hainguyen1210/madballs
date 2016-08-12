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
import madballs.collision.GiveWeaponEffect;
import madballs.effectState.EffectState;

/**
 *
 * @author haing
 */
public class EffectItem extends Item{
  
  public EffectItem(Environment environment, double x, double y, boolean isSettingDisplay, EffectState effectState) {
    super(environment, x, y, isSettingDisplay);
    setCollisionEffect(new GiveStateEffect(null, effectState));
  }  
  
  @Override
  public void setDisplayComponents() {
    setColor(Paint.valueOf("yellow"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
  }
}
