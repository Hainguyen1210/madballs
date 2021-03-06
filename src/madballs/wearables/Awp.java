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

/*
AWP is a sniper rifle which can deal huge damage but with slow firerate
Also AWP allows the owner to see further than other players
 */
public class Awp extends Weapon{
    private final double WIDTH = 50;
    private final double HEIGHT = 5;
    
    public Awp(GameObject owner, Integer id) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);
        
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(3);
        setScope(2);
        setDamage(75);
        setAmmo(10);
        setFireRate(0.4);
        setRange(1000);
        setProjectileSpeed(1100);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("yellow"));
        setProjectileImageName("bullet3");

        setFireSoundFX("awp");
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-15);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("yellow")));
        setImage("awp");
        configImageView(-15, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
