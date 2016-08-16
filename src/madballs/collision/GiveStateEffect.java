/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.effectState.BuffState;

/**
 *
 * @author haing
 */
public class GiveStateEffect extends StackedCollisionEffect{
    private BuffState effectState;

    public BuffState getEffectState() {
      return effectState;
    }
    
    public GiveStateEffect(StackedCollisionEffect effect, BuffState effectState) {
      super(effect);
      this.effectState = effectState;
    }

}
