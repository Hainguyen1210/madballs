/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class NullEffect extends StackedCollisionEffect{
    
    public NullEffect(StackedCollisionEffect effect) {
        super(effect);
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        
    }
}
