/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.DamageEffect;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.GameObject;

/**
 *
 * @author Caval
 */
public class Minigun extends Weapon{
    private final double WIDTH = 40;
    private final double HEIGHT = 15;

    public Minigun(GameObject owner, Integer id) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);
        
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(6);
        setScope(1.3);
        setDamage(15);
        setAmmo(50);
        setFireRate(10);
        setRange(800);
        setProjectileSpeed(700);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("blue"));
        setProjectileImageName("bullet2");
        
        setFireSoundFX("minigun");
    }

    
    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-10);
        setHeight(HEIGHT/2-5);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("blue")));
        setImage("minigun");
        configImageView(-10, -HEIGHT/2, HEIGHT, WIDTH);
    }

}
