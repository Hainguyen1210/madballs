/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * the effect of a collision
 * @author Caval
 */
public interface CollisionEffect {
    void affect(GameObject source, GameObject target, Shape collisionShape);
}
