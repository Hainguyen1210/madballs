/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import madballs.Collision.PushBackEffect;
import madballs.Collision.VulnerableBehaviour;
import javafx.scene.shape.Circle;
import madballs.Collision.BoostReceivableBehaviour;
import madballs.Collision.GetWeaponBehaviour;
import madballs.Collision.PushableBehaviour;
import madballs.Wearables.Pistol;
import madballs.Wearables.Weapon;
import madballs.effectState.EffectState;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private final int SPEED = 200;
    private EffectState effectState;

    public EffectState getEffectState() {
        return effectState;
    }

    public void setEffectState(EffectState effectState) {
        this.effectState = effectState;
    }
    
    public void addEffectState(EffectState effectState) {
        effectState.setWrappedEffectState(this.effectState);
        this.effectState = effectState;
    }


    public Ball(Environment environment, double a, double b) {
        super(environment, a , b, true);
        setMoveBehaviour(new StraightMove(this, SPEED));
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BoostReceivableBehaviour(null)))));
        
        weapon = new Pistol(this);
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
      try {
        this.weapon.die();
        weapon.getClass().getDeclaredConstructor(GameObject.class).newInstance(this);
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
        if (effectState != null) effectState.update(now);
        getMoveBehaviour().move(now);
    }
}
