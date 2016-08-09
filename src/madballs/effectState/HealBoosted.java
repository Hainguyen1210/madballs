/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

import madballs.Ball;

/**
 *
 * @author chim-
 */
public class HealBoosted extends EffectState{
    private int heal;
    
    public HealBoosted(Ball ball, EffectState effectState, int duration,int heal) {
        super(ball, effectState, duration);
        this.heal = heal;
        ball.setHpValue(100);
    }

    @Override
    public void fade() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void uniqueUpdate(long timestamp) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
