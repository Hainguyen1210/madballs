/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 * a collision effect that deals damage to the target
 * @author Caval
 */
public class DamageEffect extends StackedCollisionEffect{
    private double damage;
    
    public DamageEffect(StackedCollisionEffect effect, double amount){
        super(effect);
        this.damage = amount;
    }

    @Override
    public double getDamage() {
        return damage;
    }
    
}
