/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Wearables;

import madballs.Collision.DamageEffect;
import madballs.*;
import madballs.Collision.DisappearBehaviour;
import madballs.Collision.InvulnerableBehaviour;
import madballs.Collision.NullEffect;

/**
 *
 * @author Caval
 */
public class Pistol extends Weapon{

    public Pistol(Ball owner) {
        super(owner);
        
        setDamage(5);
        setAmmo(-1);
        setFireRate(1);
        setRange(500);
        setProjectileSpeed(500);
        
        setCollisionEffect(new NullEffect(null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
        
        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
        setProjectileCollisionBehaviour(new DisappearBehaviour(null));
    }
    
}
