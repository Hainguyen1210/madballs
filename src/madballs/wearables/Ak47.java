package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.DamageEffect;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.GameObject;

/*
Ak47 is an assault rifle that can deal high damage with average firerate
 */
public class Ak47 extends Weapon{
    private final double WIDTH = 40;
    private final double HEIGHT = 5;
    
    public Ak47(GameObject owner, Integer id) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);
        
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(2);
        setScope(1.2);
        setDamage(20);
        setAmmo(45);
        setFireRate(3);
        setRange(700);
        setProjectileSpeed(700);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("orange"));
        setProjectileImageName("bullet2");
        
        setFireSoundFX("ak47");
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-10);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("orange")));
        setImage("ak47");
        configImageView(-10, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
