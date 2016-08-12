/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.effectState.EffectState;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class GiveStateEffect extends StackedCollisionEffect{
    private EffectState effectState;

    public EffectState getEffectState() {
      return effectState;
    }
    
    public GiveStateEffect(StackedCollisionEffect effect, EffectState effectState) {
      super(effect);
      this.effectState = effectState;
    }     
    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
        if (wrappedEffect != null) wrappedEffect.affect(source, target, collisionShape);
    }
}
