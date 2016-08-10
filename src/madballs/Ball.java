/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import madballs.collision.PushBackEffect;
import madballs.collision.VulnerableBehaviour;
import javafx.scene.shape.Circle;
import madballs.collision.PushableBehaviour;
import madballs.wearables.Weapon;
import madballs.wearables.Awp;

import madballs.collision.GetWeaponBehaviour;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private int speed = 200;


    public Ball(Environment environment, double x, double y) {
        super(environment, x , y, true);
        setMoveBehaviour(new StraightMove(this, speed));
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(null))));
        
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

    public void setWeapon(Weapon weapon) {
      try {
        this.weapon.die();
          this.weapon = weapon.getClass().getDeclaredConstructor(GameObject.class).newInstance(this);
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
    public void updateUnique(long now) {
        getMoveBehaviour().move(now);
    }
}
