/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import madballs.Collision.PushBackEffect;
import madballs.Collision.VulnerableBehaviour;
import javafx.scene.shape.Circle;
import madballs.Collision.PushableBehaviour;
import madballs.Wearables.Pistol;
import madballs.Wearables.Weapon;
import madballs.Wearables.Awp;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private int speed = 200;


    public Ball(Environment environment, double a, double b) {
        super(environment, a , b, true);
        setMoveBehaviour(new StraightMove(this, speed));
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new VulnerableBehaviour(new PushableBehaviour(null)));
        
        weapon = new Awp(this);
    }

    public void setSpeed(int speed) {
      this.speed = speed;
    }

    public int getSpeed() {
      return speed;
    }
    public Weapon getWeapon() {
        return weapon;
    }

    public <W extends Weapon> void setWeapon(Class<W> weaponClass) {
        try {
            
            this.weapon = weaponClass.getDeclaredConstructor(getClass()).newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * set the hitBox and image of the Ball
     */
    @Override
    public void setDisplayComponents(){
        setHitBox(new Circle(15));
    }

    @Override
    public void update(long now) {
        getMoveBehaviour().move(now);
    }
}
