/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

import javafx.scene.paint.Paint;
import madballs.Ball;
import madballs.SceneManager;

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

    public BuffState(BuffState buffState, int duration) {
        wrappedBuffState = buffState;
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
            ball.setBuffState(removeFromBuffState(ball.getBuffState()));
        }
        if (wrappedBuffState != null) wrappedBuffState.update(timestamp);
    }

    public void setWrappedBuffState(BuffState wrappedBuffState) {
        this.wrappedBuffState = wrappedBuffState;
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
        apply();
        SceneManager.getInstance().displayLabel(getClass().getSimpleName(), Paint.valueOf("red"), 2.5, ball);
        if (wrappedBuffState != null) {
            wrappedBuffState.castOn(ball);
        }
    }

    public BuffState removeFromBuffState(BuffState originBuffState){
        if (this == originBuffState){
            return this.wrappedBuffState;
        }
        BuffState checking = originBuffState;
        BuffState parent = originBuffState;
        while (checking != null){
            if (checking == this){
                parent.setWrappedBuffState(checking.wrappedBuffState);
                return originBuffState;
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
