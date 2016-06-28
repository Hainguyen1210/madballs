/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import madballs.GameObject;

/**
 * the effect of a collision
 * @author Caval
 */
public interface CollisionEffect {
    public void affect(GameObject source, GameObject target);
}
