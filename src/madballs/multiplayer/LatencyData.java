package madballs.multiplayer;

/**
 * Created by caval on 15/08/2016.
 */
public class LatencyData extends Data {
    private long sentTime, receivedTime;

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getSentTime() {
        return sentTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public long getLatency() {
        return receivedTime - sentTime;
    }

    public LatencyData(long sentTime) {
        super("latency");
        this.sentTime = sentTime;
    }
}
