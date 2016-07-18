/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import madballs.Collision.BallOnlyBehaviour;
import madballs.Collision.DisappearBehaviour;
import madballs.Environment;
import madballs.GameObject;

/**
 *
 * @author chim-
 */
public abstract class Item extends GameObject{

    public Item(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay);
        
        setCollisionPassiveBehaviour(new BallOnlyBehaviour(new DisappearBehaviour(null)));
    }

    @Override
    public void update(long now) {
    
    }
    
}