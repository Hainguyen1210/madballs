/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import java.io.Serializable;
import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.gameFX.SoundStudio;

/**
 *
 * @author Caval
 */
public abstract class StackedCollisionPassiveBehaviour implements CollisionPassiveBehaviour, Serializable{
    private StackedCollisionPassiveBehaviour wrappedBehaviour;
    private StackedCollisionPassiveBehaviour decoratorBehaviour;

    public StackedCollisionPassiveBehaviour getWrappedBehaviour() {
        return wrappedBehaviour;
    }

    protected void setWrappedBehaviour(StackedCollisionPassiveBehaviour wrappedBehaviour) {
        this.wrappedBehaviour = wrappedBehaviour;
        if (wrappedBehaviour != null && wrappedBehaviour.getDecoratorBehaviour() != this) wrappedBehaviour.setDecoratorBehaviour(this);
    }

    public StackedCollisionPassiveBehaviour getDecoratorBehaviour() {
        return decoratorBehaviour;
    }

    protected void setDecoratorBehaviour(StackedCollisionPassiveBehaviour decoratorBehaviour) {
        this.decoratorBehaviour = decoratorBehaviour;
        if (decoratorBehaviour != null && decoratorBehaviour.getWrappedBehaviour() != this) decoratorBehaviour.setWrappedBehaviour(this);
    }

    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (isConditionMet(source, target, effect, collisionShape)){
            if (effect.getSoundFX() != null) SoundStudio.getInstance().playAudio(effect.getSoundFX(), source.getTranslateX(), source.getTranslateY(), 100, 100);
            uniqueGetAffected(source, target, effect, collisionShape);
        }
        if (wrappedBehaviour != null) wrappedBehaviour.getAffected(source, target, effect, collisionShape);
    }

    public StackedCollisionPassiveBehaviour(StackedCollisionPassiveBehaviour behaviour) {
        setWrappedBehaviour(behaviour);
    }
    
    public abstract void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape);

    protected abstract boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape);
    
    /**
     * the recursive method to check whether this combo behaviour contains a specific behaviour
     * @param c
     * @return 
     */
    public boolean hasCollisionBehaviour(Class c){
        if (c.isInstance(this)) return true;
        if (wrappedBehaviour != null){
            return wrappedBehaviour.hasCollisionBehaviour(c);
        }
        else {
            return false;
        }
    }

    public void wrapBehaviour(StackedCollisionPassiveBehaviour wrappedBehaviour){
        if (getWrappedBehaviour() == null){
            setWrappedBehaviour(wrappedBehaviour);
        }
        else {
            if (getWrappedBehaviour() instanceof StackedCollisionPassiveBehaviour){
                getWrappedBehaviour().wrapBehaviour(wrappedBehaviour);
            }
        }
    }

    public void replaceBehaviour(Class targetClass, StackedCollisionPassiveBehaviour substitution){
        if (targetClass.isInstance(this)){
            if (getDecoratorBehaviour() != null){
                if (substitution == null){
                    getDecoratorBehaviour().setWrappedBehaviour(getWrappedBehaviour());
                    return;
                }
                substitution.wrapBehaviour(getWrappedBehaviour());
                getDecoratorBehaviour().setWrappedBehaviour(substitution);
            }
        }
        else {
            getWrappedBehaviour().replaceBehaviour(targetClass, substitution);
        }
    }

    public static StackedCollisionPassiveBehaviour getReplacedBehaviour(StackedCollisionPassiveBehaviour origin, Class targetClass, StackedCollisionPassiveBehaviour substitution){
        if (targetClass.isInstance(origin)){
            origin.getWrappedBehaviour().setDecoratorBehaviour(substitution);
            if (substitution == null) {
                origin.getWrappedBehaviour().setDecoratorBehaviour(null);
                return origin.getWrappedBehaviour();
            }
            substitution.wrapBehaviour(origin.getWrappedBehaviour());
            return substitution;
        }
        else {
            origin.getWrappedBehaviour().replaceBehaviour(targetClass, substitution);
            return origin;
        }
    }
    
}
