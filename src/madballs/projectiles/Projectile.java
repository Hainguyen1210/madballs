/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.projectiles;


import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.StraightMove;
import madballs.wearables.Weapon;

/**
 *
 * @author Caval
 */
public class Projectile extends GameObject {
    private Weapon sourceWeapon;

    public Projectile(Weapon sourceWeapon, Shape hitBox, String projectImageName) {
        super(sourceWeapon.getEnvironment(), 0, 0, false);
        
        this.sourceWeapon = sourceWeapon;
        setHitBox(hitBox);
//        setImage(projectImageName);
        setDisplay();
        
        // calculate the spawning location of the projectile based on the real coordinate of the weapon
        double distanceFromWeapon = sourceWeapon.getWidth() + hitBox.getBoundsInLocal().getWidth() / 2 + 5;
        double rotateDirection = Math.toRadians(sourceWeapon.getRotateAngle());
        double[] realCoordinate = sourceWeapon.getRealCoordinate();
        double realX = realCoordinate[0];
        double realY = realCoordinate[1];
        setTranslateX(realX + Math.cos(rotateDirection) * distanceFromWeapon);
        setTranslateY(realY + Math.sin(rotateDirection) * distanceFromWeapon);
        
        
        // set collision characteristics and move behaviour
        setCollisionEffect(sourceWeapon.getProjectileCollisionEffect());
        setCollisionPassiveBehaviour(sourceWeapon.getProjectileCollisionBehaviour());
        
        StraightMove straightMoveBehaviour = new StraightMove(this, sourceWeapon.getProjectileSpeed());
        straightMoveBehaviour.setNewDirection(Math.toRadians(sourceWeapon.getRotateAngle()));
        setMoveBehaviour(straightMoveBehaviour);
    }

    @Override
    public void updateUnique(long now) {
        if (getMoveBehaviour().getMovedDistance() >= sourceWeapon.getRange()){
            getEnvironment().getGround().onCollision(this, getHitBox());
        }
    }

    @Override
    public void setDisplayComponents() {
//        getHitBox().setFill(Paint.valueOf("yellow"));
    }
}
