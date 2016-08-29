/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.projectiles;


import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.ImageGenerator;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Weapon;

/**
 *
 * @author Caval
 */
public class Projectile extends GameObject {
    private Weapon sourceWeapon;

    public Weapon getSourceWeapon() {
        return sourceWeapon;
    }

    public Projectile(Weapon sourceWeapon, Shape hitBox, String projectImageName, Integer id) {
        super(sourceWeapon.getEnvironment(), 0, 0, false, id);
        
        this.sourceWeapon = sourceWeapon;
        setHitBox(hitBox);
        setImage(ImageGenerator.getInstance().getImage(projectImageName));
        setDisplay(id);

        // calculate the spawning location of the projectile based on the real coordinate of the weapon
        double distanceFromWeapon = sourceWeapon.getWidth() + hitBox.getBoundsInLocal().getWidth() / 2 + 5;
        double rotateDirection = Math.toRadians(sourceWeapon.getRotateAngle());
        double[] realCoordinate = sourceWeapon.getRealCoordinate();
        double realX = realCoordinate[0];
        double realY = realCoordinate[1];
        setTranslateX(realX + Math.cos(rotateDirection) * distanceFromWeapon);
        setTranslateY(realY + Math.sin(rotateDirection) * distanceFromWeapon);

//        getImage().setFitWidth(hitBox.getLayoutBounds().getWidth());
        getImage().setFitHeight(hitBox.getLayoutBounds().getHeight());
        getImage().setTranslateX(-hitBox.getLayoutBounds().getWidth()/2);
        getImage().setTranslateY(-hitBox.getLayoutBounds().getHeight()/2);
        setRotate(Math.toRadians(sourceWeapon.getRotateAngle()));

        // set collision characteristics and move behaviour
        setCollisionEffect(sourceWeapon.getProjectileCollisionEffect());
        setCollisionPassiveBehaviour(sourceWeapon.getProjectileCollisionBehaviour());

        StraightMove straightMoveBehaviour = new StraightMove(this, sourceWeapon.getProjectileSpeed());
        straightMoveBehaviour.setNewDirection(Math.toRadians(sourceWeapon.getRotateAngle()));
        setMoveBehaviour(straightMoveBehaviour);
        getHitBox().setOpacity(0);
        sourceWeapon.setAmmo(sourceWeapon.getAmmo() - 1);
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
