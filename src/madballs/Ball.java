/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import madballs.collision.BoostReceivableBehaviour;
import madballs.collision.GetWeaponBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.VulnerableBehaviour;
import madballs.effectState.EffectState;
import madballs.wearables.Pistol;
import madballs.wearables.Weapon;
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


    public Ball(Environment environment, double x, double y) {
        super(environment, x , y, true);
        setMoveBehaviour(new StraightMove(this, SPEED));
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BoostReceivableBehaviour(null)))));
        
        weapon = new Pistol(this);
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Class<Weapon> weaponClass) {
        try {
            this.weapon.die();
            this.weapon = weaponClass.getDeclaredConstructor(GameObject.class).newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * set the hitBox and image of the Ball
     */
    @Override
    public void setDisplayComponents(){
        final ProgressBar hpBar = new ProgressBar();
        hpBar.setTranslateX(-20);
        hpBar.setTranslateY(20);
        hpBar.progressProperty().bind(new SimpleDoubleProperty(getHpValue()/100.0));
        hpBar.setPrefWidth(40);
        hpBar.getStyleClass().add("hp-bar");
        
        getStatusG().getChildren().add(hpBar);
//        hpBar.setLayoutY(getTranslateY() - 1);
        
        setHitBox(new Circle(15));
    }
    @Override
    public void updateUnique(long now) {
        if (effectState != null) effectState.update(now);
        getMoveBehaviour().move(now);
    }
}
