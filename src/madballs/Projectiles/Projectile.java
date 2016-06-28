/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Projectiles;

import madballs.Environment;
import madballs.GameObject;
import madballs.MoveBehaviour;
import madballs.StraightMove;
import madballs.Wearables.Weapon;

/**
 *
 * @author Caval
 */
public class Projectile extends GameObject{
    private MoveBehaviour moveBehaviour;
    private Weapon sourceWeapon;    

    public Projectile(Weapon sourceWeapon) {
        super(sourceWeapon.getEnvironment(), sourceWeapon.getTranslateX(), sourceWeapon.getTranslateY());
        this.sourceWeapon = sourceWeapon;
        
        setCollisionEffect(sourceWeapon.getProjectileCollisionEffect());
        setCollisionPassiveBehaviour(sourceWeapon.getCollisionPassiveBehaviour());
        
        setMoveBehaviour(new StraightMove(this, sourceWeapon.getProjectileSpeed()));
        if (sourceWeapon.getRange() != -1){
            double angle = Math.toRadians(sourceWeapon.getDisplay().getRotate());
            double targetX = sourceWeapon.getTranslateX() + Math.cos(sourceWeapon.getRange());
            double targetY = sourceWeapon.getTranslateY() + Math.sin(sourceWeapon.getRange());
            moveBehaviour.setTargetX(targetX);
            moveBehaviour.setTargetY(targetY);
            moveBehaviour.setDirection(angle);
        }
    }

    @Override
    public void update(long now) {
        
    }
}
