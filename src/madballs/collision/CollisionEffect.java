/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * the active collision effect of a GameObject. E.g. the CollisionEffect of obj A would determine how obj A effect obj B when they collide.
 * @author Caval
 */
public interface CollisionEffect {
    void affect(GameObject source, GameObject target, Shape collisionShape);
}
