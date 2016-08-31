/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import java.io.Serializable;
import javafx.scene.shape.Shape;
import madballs.Environment;
import madballs.GameObject;
import madballs.gameFX.SoundStudio;

/**
 *
 * @author Caval
 */
public abstract class StackedCollisionEffect implements CollisionEffect, Serializable{
    StackedCollisionEffect wrappedEffect;
    private String soundFX;

    public void setSoundFX(String soundFX) {
        this.soundFX = soundFX;
    }

    public String getSoundFX() {
        return soundFX;
    }

    public StackedCollisionEffect(StackedCollisionEffect effect) {
        wrappedEffect = effect;
    }

    public CollisionEffect getWrappedEffect() {
        return wrappedEffect;
    }
    
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        if (wrappedEffect != null) wrappedEffect.affect(source, target, collisionShape);
    }

    /**
     * the recursive method to check whether this combo effect contains a specific effect
     * @param c
     * @return
     */
    public boolean hasCollisionEffect(Class c){
        if (c.isInstance(this)) return true;
        if (wrappedEffect != null && wrappedEffect instanceof StackedCollisionEffect){
            return ((StackedCollisionEffect) wrappedEffect).hasCollisionEffect(c);
        }
        else {
            return false;
        }
    }
}
