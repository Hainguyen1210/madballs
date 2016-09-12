/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

/**
 * can push back other objects
 * @author Caval
 */
public class PushBackEffect extends StackedCollisionEffect{
    private double pushBackAmount;

    public PushBackEffect(double pushBackAmount, StackedCollisionEffect effect) {
        super(effect);
        this.pushBackAmount = pushBackAmount;
    }
    
    public double getPushBackAmount(){
        return pushBackAmount;
    }
    
}
