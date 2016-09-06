package madballs.buffState;

import javafx.scene.paint.Color;
import madballs.multiplayer.BuffData;

/**
 * Created by caval on 01/09/2016.
 */
public class Armor extends BuffState {
    private double value;
    private double originHp;

    public Armor(BuffState buffState, int duration, double value) {
        super(buffState, duration);
        this.value = value;
    }

    public Armor(BuffData data) {
        super(data);
    }

    @Override
    public double[] getParameters() {
        return new double[] {value, originHp};
    }

    @Override
    public void recreateFromData(BuffData data) {
        this.value = data.getParameters()[0];
        this.originHp = data.getParameters()[1];
    }

    @Override
    public void apply() {
        getBall().setMaxHp(getBall().getMaxHp() + value);
        originHp = getBall().getHpValue();
        getBall().setHpValue(originHp + value);
    }

    @Override
    public void fade() {
        getBall().setMaxHp(getBall().getMaxHp() - value);
    }

    @Override
    public void setColor() {
        setColor(Color.BLACK);
    }

    @Override
    public void uniqueUpdate(long timestamp) {
        if (getBall().getHpValue() <= originHp){
            forceFade();
        }
    }
}
