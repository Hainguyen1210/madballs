/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.ImageGenerator;
import madballs.collision.DamageEffect;
import madballs.collision.DisappearBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.WeaponIgnoredBehaviour;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class Uzi extends Weapon{
    private final double WIDTH = 25;
    private final double HEIGHT = 7;

    public Uzi(GameObject owner) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setScope(1.1);
        setDamage(10);
        setAmmo(40);
        setFireRate(4);
        setRange(700);
        setProjectileSpeed(800);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("red"));
        
        setFireSoundFX("uzi");
        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
        setProjectileCollisionBehaviour(new WeaponIgnoredBehaviour(new DisappearBehaviour(null)));
    }
    
    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("red")));
        setImage(ImageGenerator.getInstance().getImage("uzi"));
        configImageView(0, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
