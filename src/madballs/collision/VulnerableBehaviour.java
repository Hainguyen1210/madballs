/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.gameFX.SoundStudio;

/**
 *
 * @author Caval
 */
public class VulnerableBehaviour extends StackedCollisionPassiveBehaviour{

    public VulnerableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        SoundStudio.getInstance().playAudio("penetrate");
        target.setHpValue(target.getHpValue() - effect.getDamage());
        System.out.println(target.getHpValue());
        if (target.getHpValue() <= 0){
            target.die();
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect.hasCollisionEffect(DamageEffect.class);
    }

}
