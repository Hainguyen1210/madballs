/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class StackedCollisionEffect implements CollisionEffect{
    StackedCollisionEffect wrappedEffect;

    
    public StackedCollisionEffect(StackedCollisionEffect effect) {
        wrappedEffect = effect;
    }

    public CollisionEffect getWrappedEffect() {
        return wrappedEffect;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        if (wrappedEffect != null) wrappedEffect.affect(source, target, collisionShape);
    }
    
    /**
     * the recursive method to check whether this combo effect contains a specific effect
     * @param effectClass
     * @return 
     */
    public boolean hasCollisionEffect(Class effectClass){
        if (effectClass.isInstance(this)) return true;
        if (wrappedEffect != null && wrappedEffect instanceof StackedCollisionEffect){
            return ((StackedCollisionEffect) wrappedEffect).hasCollisionEffect(effectClass);
        }
        else {
            return false;
        }
    }
    
    /**
     * return the total damage caused by this combo effect
     * @return 
     */
    public double getDamage(){
        double damage = this instanceof DamageEffect ? ((DamageEffect)this).getDamage() : 0;
        return wrappedEffect == null ? damage : wrappedEffect.getDamage() + damage;
    }
    
    /**
     * return the total push back amount caused by this combo effect
     * @return 
     */
    public double getPushBackAmount(){
        double amount = this instanceof PushBackEffect ? ((PushBackEffect)this).getPushBackAmount(): 0;
        return wrappedEffect == null ? amount : wrappedEffect.getPushBackAmount() + amount;
    }
}
