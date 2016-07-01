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
public class DisappearBehaviour extends StackedCollisionPassiveBehaviour{

    public DisappearBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        System.out.println(PushBackEffect.class);
        if (effect.hasCollisionEffect(PushBackEffect.class)) {
            System.out.println("123");
            target.getEnvironment().removeGameObj(target);
        }
        super.getAffected(source, target, effect, collisionShape);
    }
    
}
