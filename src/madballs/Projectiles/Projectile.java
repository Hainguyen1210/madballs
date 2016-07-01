/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Projectiles;

import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.StraightMove;
import madballs.Wearables.Weapon;

/**
 *
 * @author Caval
 */
public class Projectile extends GameObject{
    private Weapon sourceWeapon;    

    public Projectile(Weapon sourceWeapon, Shape hitBox, Image image) {
        super(sourceWeapon.getEnvironment(), 0, 0, false);
//        super(sourceWeapon.getEnvironment(), 400, 400, false);
        this.sourceWeapon = sourceWeapon;
        setHitBox(hitBox);
        setImage(image);
        setDisplay();
        
        double distanceFromWeapon = sourceWeapon.getWidth() + hitBox.getBoundsInLocal().getWidth() * 2;
        double rotateDirection = Math.toRadians(sourceWeapon.getRotateAngle());
        double[] realCoordinate = sourceWeapon.getRealCoordinate();
        double realX = realCoordinate[0];
        double realY = realCoordinate[1];
        setTranslateX(realX + Math.cos(rotateDirection) * distanceFromWeapon);
        setTranslateY(realY + Math.sin(rotateDirection) * distanceFromWeapon);
        
        setCollisionEffect(sourceWeapon.getProjectileCollisionEffect());
        setCollisionPassiveBehaviour(sourceWeapon.getProjectileCollisionBehaviour());
        
        setMoveBehaviour(new StraightMove(this, sourceWeapon.getProjectileSpeed()));
        if (sourceWeapon.getRange() != -1){
            double angle = Math.toRadians(sourceWeapon.getRotateAngle());
//            System.out.println(getMoveBehaviour() == null);
            getMoveBehaviour().setDirection(angle);
        }
    }

    @Override
    public void update(long now) {
        getMoveBehaviour().move(now);
//        if (getTranslateX() == getMoveBehaviour().getTargetX()){
//            getEnvironment().removeGameObj(this);
//        }
    }

    @Override
    public void setDisplayComponents() {
//        getHitBox().setFill(Paint.valueOf("yellow"));
    }
}
