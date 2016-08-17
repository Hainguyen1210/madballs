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
public abstract class BuffState {
    private BuffState wrappedBuffState;
    private long createdTime = 0;
    private int duration;
    private long lastTick = 0;
    private long tickInterval = 1;
    private Ball ball;

    public long getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(long tickInterval) {
        this.tickInterval = tickInterval;
    }

    public long getLastTick() {
        return lastTick;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public BuffState(BuffState effectState, int duration) {
        wrappedBuffState = effectState;
        this.duration = duration;
        
    }
    public void update(long timestamp){
        if (createdTime == 0) {
            createdTime = timestamp;
        }
        if ((timestamp - createdTime) / 1000000000 <= duration){
            if ((timestamp - lastTick) / 1000000000 >= tickInterval){
                lastTick = timestamp;
                uniqueUpdate(timestamp);
            }
        }
        else {
            fade();
            ball.setEffectState(removeFromBuffState(ball.getEffectState()));
        }
    }

    public void setWrappedBuffState(BuffState wrappedEffectState) {
        this.wrappedBuffState = wrappedEffectState;
    }

    public BuffState getWrappedBuffState() {
        return wrappedBuffState;
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

    public void castOn(Ball ball) {
      this.ball = ball;
      ball.addEffectState(this);
      apply();
    }

    public BuffState removeFromBuffState(BuffState originEffectState){
        if (this == originEffectState){
            return this.wrappedBuffState;
        }
        BuffState checking = originEffectState;
        BuffState parent = originEffectState;
        while (checking != null){
            if (checking == this){
                parent.setWrappedBuffState(checking.wrappedBuffState);
                return originEffectState;
            }
            parent = checking;
            checking = parent.wrappedBuffState;
        }
        return null;
    }
    
    public abstract void apply();
    public abstract void fade();
    public abstract void uniqueUpdate(long timestamp);
}
