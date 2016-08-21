package madballs.multiplayer;

import madballs.buffState.BuffState;

/**
 * Created by caval on 18/08/2016.
 */
public class BuffData extends Data {
    private BuffData wrappedBuffData;
    private String buffStateClass;
    private long createdTime = 0;
    private int duration;
    private long lastTick = 0;
    private long tickInterval = 1;
    private Integer ballID;
    private double[] parameters;

    public BuffData getWrappedBuffData() {
        return wrappedBuffData;
    }

    public String getBuffStateClass() {
        return buffStateClass;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public int getDuration() {
        return duration;
    }

    public long getLastTick() {
        return lastTick;
    }

    public long getTickInterval() {
        return tickInterval;
    }

    public Integer getBallID() {
        return ballID;
    }

    public double[] getParameters() {
        return parameters;
    }

    public BuffData(BuffState buffState) {
        super("buff");
        if (buffState.getWrappedBuffState() != null){
            wrappedBuffData = new BuffData(buffState.getWrappedBuffState());
        }
        buffStateClass = buffState.getClass().getName();
        createdTime = buffState.getCreatedTime();
        duration = buffState.getDuration();
        lastTick = buffState.getLastTick();
        tickInterval = buffState.getTickInterval();
        System.out.println("asd" + buffState.getClass());
        ballID = buffState.getBall().getID();
        parameters = buffState.getParameters();
    }
}
