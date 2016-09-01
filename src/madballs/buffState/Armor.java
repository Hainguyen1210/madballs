package madballs.buffState;

import javafx.scene.paint.Color;
import madballs.multiplayer.BuffData;

/**
 * Created by caval on 01/09/2016.
 */
public class Armor extends BuffState {
    private double value;
    private double lastHP;

    public Armor(BuffState buffState, int duration, double value) {
        super(buffState, duration);
        this.value = value;
    }

    public Armor(BuffData data) {
        super(data);
    }

    @Override
    public double[] getParameters() {
        return new double[] {value, lastHP};
    }

    @Override
    public void recreateFromData(BuffData data) {
        this.value = data.getParameters()[0];
        this.lastHP = data.getParameters()[1];
    }

    @Override
    public void apply() {
        lastHP = getBall().getHpValue();
    }

    @Override
    public void fade() {

    }

    @Override
    public void setColor() {
        setColor(Color.BLACK);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
        double lostHP = lastHP - getBall().getHpValue();
        if (lostHP > 0){
            value -= lostHP;
            getBall().setHpValue(lastHP);
            if (value < 0){
                getBall().setHpValue(lastHP - value);
                forceFade();
            }
        }
        lastHP = getBall().getHpValue();
    }
}
