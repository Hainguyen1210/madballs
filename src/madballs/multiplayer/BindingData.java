package madballs.multiplayer;

/**
 * the Data informing the binding of one GameObject to another
 * Created by caval on 04/09/2016.
 */
public class BindingData extends Data {
    private Integer originID, binderID;
    private double xDiff, yDiff;

    public Integer getOriginID() {
        return originID;
    }

    public Integer getBinderID() {
        return binderID;
    }

    public double getXDiff() {
        return xDiff;
    }

    public double getYDiff() {
        return yDiff;
    }

    public BindingData(Integer originID, Integer binderID, double xDiff, double yDiff) {
        super("binding");
        this.originID = originID;
        this.binderID = binderID;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
    }
}
