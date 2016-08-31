/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.multiplayer.GetWeaponData;
import madballs.wearables.Weapon;

/**
 *
 * @author haing
 */
public class GetWeaponBehaviour extends StackedCollisionPassiveBehaviour{
  
  public GetWeaponBehaviour(CollisionPassiveBehaviour behaviour) {
    super(behaviour);
  }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
//        System.out.println("give weap by " + source);
        Weapon weapon = ((GiveWeaponEffect)effect).getWeapon();
        if(weapon != null && MadBalls.isHost()) {
            ((Ball)target).setWeapon(weapon.getClass(), -1);
        }
        GameObject owner = source.getOwner();
        if (owner != null) {
            owner.die();
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof GiveWeaponEffect;
    }

}
