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
public class VulnerableBehaviour extends StackedCollisionPassiveBehaviour{

    public VulnerableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (effect.hasCollisionEffect(DamageEffect.class)){
            target.setHpValue(target.getHpValue() - effect.getDamage());
            System.out.println(target.getHpValue());
            if (target.getHpValue() <= 0){
                target.die();
            }
        }
        super.getAffected(source, target, effect, collisionShape);
    }
    
}
