package madballs.buffState;

import javafx.scene.paint.Color;
import madballs.collision.InvulnerableBehaviour;
import madballs.collision.StackedCollisionPassiveBehaviour;
import madballs.collision.VulnerableBehaviour;
import madballs.multiplayer.BuffData;

/**
 * make ball invulnerable
 * Created by caval on 03/09/2016.
 */
public class Invulnerable extends BuffState {
    public Invulnerable(BuffState buffState, int duration) {
        super(buffState, duration);
    }

    public Invulnerable(BuffData data) {
        super(data);
    }

    @Override
    public double[] getParameters() {
        return new double[0];
    }

    @Override
    public void recreateFromData(BuffData data) {

    }

    /**
     * change vulnerable to invulnerable
     */
    @Override
    public void apply() {
        if (getBall().getCollisionPassiveBehaviour() instanceof StackedCollisionPassiveBehaviour){
            getBall().setCollisionPassiveBehaviour(StackedCollisionPassiveBehaviour.getReplacedBehaviour(
                    (StackedCollisionPassiveBehaviour) getBall().getCollisionPassiveBehaviour(),
                    VulnerableBehaviour.class,
                    new InvulnerableBehaviour(null)));
        }
    }

    /**
     * change invulnerable to vulnerable
     */
    @Override
    public void fade() {
        if (getBall().getCollisionPassiveBehaviour() instanceof StackedCollisionPassiveBehaviour){
            getBall().setCollisionPassiveBehaviour(StackedCollisionPassiveBehaviour.getReplacedBehaviour(
                    (StackedCollisionPassiveBehaviour) getBall().getCollisionPassiveBehaviour(),
                    InvulnerableBehaviour.class,
                    new VulnerableBehaviour(null)));
        }
    }

    @Override
    public void setColor() {
        setColor(Color.ORANGE);
    }

    @Override
    public void uniqueUpdate(long timestamp) {

    }
}
