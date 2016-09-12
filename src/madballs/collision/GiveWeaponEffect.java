/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import madballs.wearables.Weapon;

/**
 * can give Weapon to another object
 * @author haing
 */
public class GiveWeaponEffect extends StackedCollisionEffect {
    private Weapon weapon;

    public Weapon getWeapon() {
        return weapon;
    }

    public GiveWeaponEffect(Weapon weapon, StackedCollisionEffect effect) {
        super(effect);
        this.weapon = weapon;
    }

}
