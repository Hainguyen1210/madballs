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
public abstract class EffectState {
    private EffectState wrappedEffectState;
    private int duration;
    private long createdTime = 0;
    private Ball ball;

    public EffectState(Ball ball, EffectState effectState, int duration) {
        wrappedEffectState = effectState;
        this.ball = ball;
        this.duration = duration;
        
    }
    public void update(long timestamp){
        if (createdTime == 0) {
            createdTime = timestamp;
        }
        else if ((timestamp - createdTime) / 1000000000 <= duration){
            uniqueUpdate(timestamp);
        }
        else {
            fade();
            ball.setEffectState(removeFromEffectState(ball.getEffectState()));
        }
    }

    public void setWrappedEffectState(EffectState wrappedEffectState) {
        this.wrappedEffectState = wrappedEffectState;
    }

    public EffectState getWrappedEffectState() {
        return wrappedEffectState;
    }

    public int getDuration() {
        return duration;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public Ball getBall() {
        return ball;
    }
    
    public EffectState removeFromEffectState(EffectState originEffectState){
        
        EffectState checking = originEffectState;
        EffectState parent = originEffectState;
        while (checking != null){
            if (checking == this){ 
                if (parent == this) {
                    return checking.wrappedEffectState;
                }
                else {
                    parent.setWrappedEffectState(checking.wrappedEffectState);
                    return parent;
                }
            }
            parent = checking;
            checking = parent.wrappedEffectState;
        }
        return null;
    }
    
    public abstract void fade();
    public abstract void uniqueUpdate(long timestamp);
}
