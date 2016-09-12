package madballs.collision;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Shape;
import madballs.Flag;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.multiplayer.BindingData;
import madballs.scenes.SceneManager;

/**
 * allows the GameObject to bind with another one when they collides
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
        MadBalls.getMultiplayerHandler().sendData(new BindingData(source.getID(), target.getID(), xDiff, yDiff));
        source.deadProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    target.die();
                }
            }
        });
        if (target instanceof Flag) {
            SceneManager.getInstance().announceFlag(source.getID(), target.getID(), "picked up");
            ((Flag)target).setCarrierID(source.getID());
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return MadBalls.isHost() && !source.isDead() &&
                ((!target.getTranslateXProperty().isBound() && !target.getTranslateYProperty().isBound()) || (shouldSwitchOwner));
    }
}
