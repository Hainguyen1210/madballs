/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import madballs.Environment;
import madballs.effectState.SpeedBoosted;


/**
 *
 * @author haing
 */
public class SpeedBoost extends EffectItem{
  public SpeedBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
    super(environment, x, y, isSettingDisplay, new SpeedBoosted(null, null, 5, 50));
  }
}
