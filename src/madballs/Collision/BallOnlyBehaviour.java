/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;

/**
 *
 * @author chim-
 */
public class BallOnlyBehaviour extends StackedCollisionPassiveBehaviour{
    
    public BallOnlyBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (source instanceof  Ball) {
            super.getAffected(source, target, effect, collisionShape);
        }
    }
}
