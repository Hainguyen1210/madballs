/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;

import madballs.Ball;
import madballs.scenes.SceneManager;
import madballs.multiplayer.BuffData;

/**
 * create buff effect instantly 
 * @author caval
 */
public abstract class InstantBuff extends BuffState{

    /**
     * create buff state by data received from server
     * @param data
     */
    public InstantBuff(BuffData data){
        super(data);
    }

    public InstantBuff(BuffState buffState) {
        super(buffState, 0);
    }
    
    @Override
    public void castOn(Ball ball, int index){
        setBall(ball);
        SceneManager.getInstance().displayLabel(getClass().getSimpleName(), getColor(), 0.75, ball, index * 0.375);
        apply();
        if (getWrappedBuffState() != null) {
            getWrappedBuffState().castOn(ball, index == 0 ? 0 : index + 1);
        }
    }

    @Override
    public void fade() {}

    @Override
    public void uniqueUpdate(long timestamp) {}
    
}
