package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * get affected by DamageEffect for certain amount of times
 * Created by caval on 01/09/2016.
 */
public class QuantitativeVulnerableBehaviour extends VulnerableBehaviour {
    private int counter = 1;
    private int quantity;

    public QuantitativeVulnerableBehaviour(int quantity, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.quantity = quantity;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (counter < quantity){
            counter++;
        }
        else {
            target.setHpValue(0);
        }
        super.uniqueGetAffected(source, target, effect, collisionShape);
    }
}
