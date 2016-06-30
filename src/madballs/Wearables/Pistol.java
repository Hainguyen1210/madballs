/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Wearables;

import javafx.scene.shape.Rectangle;
import madballs.Ball;
import madballs.Collision.DamageEffect;
import madballs.Collision.DisappearBehaviour;
import madballs.Collision.InvulnerableBehaviour;
import madballs.Collision.NullEffect;
import madballs.Collision.PushBackEffect;
import madballs.Collision.PushableBehaviour;

/**
 *
 * @author Caval
 */
public class Pistol extends Weapon{
    private final double WIDTH = 20;
    private final double HEIGHT = 10;

    public Pistol(Ball owner) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));
        
        
        setDamage(5);
        setAmmo(-1);
        setFireRate(1);
        setRange(500);
        setProjectileSpeed(500);
        
        setProjectileHitBoxSize(3);
        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
        setProjectileCollisionBehaviour(new DisappearBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
        setHitBox(new Rectangle(WIDTH, HEIGHT));
    }
    
}
