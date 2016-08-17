/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

import madballs.Ball;

/**
 *
 * @author caval
 */
public abstract class InstantBuff extends BuffState{

    public InstantBuff(BuffState buffState) {
        super(buffState, 0);
    }
    
    @Override
    public void castOn(Ball ball){
        setBall(ball);
        apply();
    }

    @Override
    public void fade() {}

    @Override
    public void uniqueUpdate(long timestamp) {}
    
}
