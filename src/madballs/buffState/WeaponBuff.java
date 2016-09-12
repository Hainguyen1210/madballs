package madballs.buffState;

import madballs.multiplayer.BuffData;

/**
 * used for maintaining buff when pickup other weapon
 * Created by caval on 18/08/2016.
 */
public abstract class WeaponBuff extends BuffState {
    public WeaponBuff(BuffState buffState, int duration) {
        super(buffState, duration);
    }

    public WeaponBuff(BuffData data) {
        super(data);
    }
}
