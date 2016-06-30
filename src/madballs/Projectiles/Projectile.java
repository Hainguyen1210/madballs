/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Projectiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
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

    public Projectile(Weapon sourceWeapon, Shape hitBox, Image image) {
        super(sourceWeapon.getEnvironment(), sourceWeapon.getTranslateX(), sourceWeapon.getTranslateY());
        this.sourceWeapon = sourceWeapon;
        setHitBox(hitBox);
        setImage(new ImageView(image));
        
        setCollisionEffect(sourceWeapon.getProjectileCollisionEffect());
        setCollisionPassiveBehaviour(sourceWeapon.getCollisionPassiveBehaviour());
        
        setMoveBehaviour(new StraightMove(this, sourceWeapon.getProjectileSpeed()));
        if (sourceWeapon.getRange() != -1){
            double angle = Math.toRadians(sourceWeapon.getDisplay().getRotate());
            
            moveBehaviour.setDirection(angle);
        }
    }

    @Override
    public void update(long now) {
        moveBehaviour.move(now);
        if (getTranslateX() == moveBehaviour.getTargetX()){
            getEnvironment().removeGameObj(this);
        }
    }

    @Override
    public void setDisplayComponents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
