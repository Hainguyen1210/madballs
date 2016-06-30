/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class VulnerableBehaviour extends StackedCollisionPassiveBehaviour{

    public VulnerableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        target.setHp(target.getHp() - effect.getDamage());
        super.getAffected(source, target, effect, collisionShape);
    }
    
}
