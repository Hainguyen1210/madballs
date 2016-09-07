package madballs.buffState;

import madballs.collision.*;
import madballs.multiplayer.BuffData;

/**
 * Created by caval on 07/09/2016.
 */
public class NewBorn extends Invulnerable {
    public NewBorn(BuffState buffState, int duration) {
        super(buffState, duration);
    }

    public NewBorn(BuffData data) {
        super(data);
    }

    @Override
    public void apply() {
        super.apply();
        getBall().setCollisionEffect(new PushBackEffect(-1, new DamageEffect(80, 1.1, getBall().getID(), null)));
    }

    @Override
    public void fade() {
        super.fade();
        getBall().setCollisionEffect(new PushBackEffect(-1, null));
    }
}
