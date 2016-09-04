package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Flag;
import madballs.GameObject;
import madballs.MadBalls;

/**
 * Created by caval on 03/09/2016.
 */
public class BindingBehaviour extends StackedCollisionPassiveBehaviour {
    private double xDiff, yDiff;
    private boolean shouldSwitchOwner;

    public BindingBehaviour(double xDiff, double yDiff, boolean shouldSwitchOwner,  StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.shouldSwitchOwner = shouldSwitchOwner;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.bindDisplay(source, xDiff, yDiff);
        if (target instanceof Flag) {
            ((Flag)target).setCarrierID(source.getID());
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return MadBalls.isHost() &&
                ((!target.getTranslateXProperty().isBound() && !target.getTranslateYProperty().isBound()) || (shouldSwitchOwner));
    }
}
