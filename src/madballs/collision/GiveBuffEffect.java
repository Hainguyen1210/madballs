/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import madballs.buffState.BuffState;

/**
 * can give BuffState to another GameObject
 * @author haing
 */
public class GiveBuffEffect extends StackedCollisionEffect{
    private BuffState buffState;

    public BuffState getBuffState() {
      return buffState;
    }
    
    public GiveBuffEffect(BuffState buffState, StackedCollisionEffect effect) {
      super(effect);
      this.buffState = buffState;
    }

}
