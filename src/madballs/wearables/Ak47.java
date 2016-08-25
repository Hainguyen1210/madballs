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

public class Ak47 extends Weapon{
    private final double WIDTH = 40;
    private final double HEIGHT = 5;
    
    public Ak47(GameObject owner) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25);
        
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setScope(1.2);
        setDamage(20);
        setAmmo(45);
        setFireRate(3);
        setRange(700);
        setProjectileSpeed(500);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("orange"));
        setProjectileImage(ImageGenerator.getInstance().getImage("bullet"));
        
        setProjectileCollisionEffect(new DamageEffect(null, getDamage()));
        setProjectileCollisionBehaviour(new WeaponIgnoredBehaviour(new DisappearBehaviour(null)));
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-10);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("orange")));
        setImage(ImageGenerator.getInstance().getImage("ak47"));
        configImageView(-10, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
