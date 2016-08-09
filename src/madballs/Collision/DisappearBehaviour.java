/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.Wearables.Weapon;

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
//        System.out.println(source.getClass);
        if (effect.hasCollisionEffect(PushBackEffect.class)) {
            target.die();
        }
        super.getAffected(source, target, effect, collisionShape);
    }
    
}
