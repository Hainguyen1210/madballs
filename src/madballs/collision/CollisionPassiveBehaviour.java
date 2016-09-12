/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * the passive collision effect of a GameObject. E.g. how obj B is affected by obj A with CollisionEffect C when they collide
 * @author Caval
 */
public interface CollisionPassiveBehaviour {
    void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape);
}
