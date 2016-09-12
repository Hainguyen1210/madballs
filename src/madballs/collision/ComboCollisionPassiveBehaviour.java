package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * this Class represents a Map<K,V> of CollisionPassiveBehaviours and their Callbacks.
 * Each time the GameObject collides, every Callback is checked to see if the corresponding CollisionPassiveBehaviour is affected. If the Callback returns True,
 * then the CollisionPassiveBehaviour's getAffected() method is called
 * Created by caval on 03/09/2016.
 */
public class ComboCollisionPassiveBehaviour extends StackedCollisionPassiveBehaviour {
    private Map<StackedCollisionPassiveBehaviour, Callback> stackedCollisionPassiveBehaviours;

    public ComboCollisionPassiveBehaviour(Map<StackedCollisionPassiveBehaviour, Callback> stackedCollisionPassiveBehaviours, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.stackedCollisionPassiveBehaviours = stackedCollisionPassiveBehaviours;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        for (StackedCollisionPassiveBehaviour stackedCollisionPassiveBehaviour: stackedCollisionPassiveBehaviours.keySet()){
            try {
                if (stackedCollisionPassiveBehaviour.isConditionMet(source, target, effect, collisionShape)){
                    if (stackedCollisionPassiveBehaviours.get(stackedCollisionPassiveBehaviour).checkCondition(source, target, effect, collisionShape)){
                        stackedCollisionPassiveBehaviour.uniqueGetAffected(source, target, effect, collisionShape);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return true;
    }
}
