/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.buffState;

import javafx.scene.paint.Paint;
import madballs.Ball;
import madballs.MadBalls;
import madballs.scenes.SceneManager;
import madballs.multiplayer.BuffData;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author chim-
 */
public abstract class BuffState{
    private BuffState wrappedBuffState;
    private long createdTime = 0;
    private int duration;
    private long lastTick = 0;
    private long tickInterval = 1;
    private Ball ball;
    private Paint color = Paint.valueOf("red");
    private boolean hasFaded = false;
    private String name = getClass().getSimpleName();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Paint getColor() {
        return color;
    }

    protected void setColor(Paint color){
        this.color = color;
    }

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
        setColor();
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
        else if (!hasFaded) {
            forceFade();
        }
        if (wrappedBuffState != null) wrappedBuffState.update(timestamp);
    }

    public void forceFade(){
        fade();
        ball.removeBuffState(this);
        if (ball == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()) SceneManager.getInstance().removeBuffState(this);
        ball.setBuffState(removeFromBuffState(ball.getBuffState()));
        hasFaded = true;
    }

    public void setWrappedBuffState(BuffState wrappedBuffState) {
        this.wrappedBuffState = wrappedBuffState;
    }

    public void wrapBuffState(BuffState buffState){
        if (wrappedBuffState != null){
            wrappedBuffState.wrapBuffState(buffState);
        }
        else {
            setWrappedBuffState(buffState);
        }
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

    public void castOn(Ball ball, int index) {
        this.ball = ball;
        apply();
        SceneManager.getInstance().displayLabel(name, color, 0.75, ball, index * 0.375);
        if (ball == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()) SceneManager.getInstance().registerBuffState(this);
        if (wrappedBuffState != null) {
            wrappedBuffState.castOn(ball, index + 1);
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

    public BuffState(BuffData data){
        createdTime = data.getCreatedTime();
        duration = data.getDuration();
        lastTick = data.getLastTick();
        tickInterval = data.getTickInterval();
        ball = (Ball) MadBalls.getMainEnvironment().getObject(data.getBallID());
        setColor();
        recreateFromData(data);
    }

    public static BuffState recreateBuffState(BuffData data){
        try {
            Class buffStateClass = Class.forName(data.getBuffStateClass());
            BuffState buffState = (BuffState) buffStateClass.getDeclaredConstructor(BuffData.class).newInstance(data);
            if (data.getWrappedBuffData() != null){
                buffState.setWrappedBuffState(recreateBuffState(data.getWrappedBuffData()));
            }
            return buffState;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <B extends BuffState> void reApply(Class<B> buffClass){
        if (buffClass.isInstance(this)){
            apply();
        }
        if (wrappedBuffState != null){
            wrappedBuffState.reApply(buffClass);
        }
    }
    public abstract double[] getParameters();
    public abstract void recreateFromData(BuffData data);
    public abstract void apply();
    public abstract void fade();
    public abstract void setColor();
    public abstract void uniqueUpdate(long timestamp);
}
