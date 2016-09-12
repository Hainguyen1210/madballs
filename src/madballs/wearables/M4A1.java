package madballs.wearables;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import madballs.collision.DamageEffect;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.GameObject;
/*
M4A1 is an assault rifle which can deal average damage with good firerate
 */
public class M4A1 extends Weapon{
    private final double WIDTH = 40;
    private final double HEIGHT = 5;
    
    public M4A1(GameObject owner, Integer id) {
        super(owner, 
                owner.getHitBox().getBoundsInLocal().getWidth() * 0.25,
                owner.getHitBox().getBoundsInLocal().getHeight() * 0.25, id);
        
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new PushableBehaviour(null));

        setWeight(2);
        setScope(1.2);
        setDamage(18);
        setAmmo(45);
        setFireRate(4);
        setRange(600);
        setProjectileSpeed(700);
        setProjectileHitBoxSize(3);
        setProjectileColor(Paint.valueOf("gray"));
        setProjectileImageName("bullet2");
        
        setFireSoundFX("m4a1");
    }

    @Override
    public void setDisplayComponents() {
        setWidth(WIDTH-10);
        setHeight(HEIGHT/2);
        setHitBox(new Rectangle(getWidth(), getHeight(), Paint.valueOf("gray")));
        setImage("m4a1");
        configImageView(-10, -HEIGHT/2, HEIGHT, WIDTH);
    }
    
}
