/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.Obstacle;

/**
 *
 * @author chim-
 */
public class Ball_n_WallBehaviour extends StackedCollisionPassiveBehaviour{
    
    public Ball_n_WallBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (source instanceof  Ball || source instanceof Obstacle) {
            super.getAffected(source, target, effect, collisionShape);
        }
    }
}
