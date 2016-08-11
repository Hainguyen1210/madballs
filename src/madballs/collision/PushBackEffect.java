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
public class PushBackEffect extends StackedCollisionEffect{
    private double pushBackAmount;

    public PushBackEffect(StackedCollisionEffect effect, double pushBackAmount) {
        super(effect);
        this.pushBackAmount = pushBackAmount;
    }
    
    public double getPushBackAmount(){
        return pushBackAmount;
    }
    
}
