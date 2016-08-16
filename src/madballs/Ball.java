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
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Circle;
import madballs.collision.BuffReceivableBehaviour;
import madballs.collision.GetWeaponBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.VulnerableBehaviour;
import madballs.effectState.BuffState;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Pistol;
import madballs.wearables.RocketLauncher;
import madballs.wearables.Weapon;
import madballs.wearables.XM1104;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private final int SPEED = 100;
    private BuffState effectState;

    public BuffState getEffectState() {
        return effectState;
    }

    public void setEffectState(BuffState effectState) {
        this.effectState = effectState;
    }
    
    public void addEffectState(BuffState effectState) {
        System.out.println("add effect" + effectState);
        effectState.setWrappedEffectState(this.effectState);
        this.effectState = effectState;
    }


    public Ball(Environment environment, double x, double y) {
        super(environment, x , y, true);
        setMoveBehaviour(new StraightMove(this, SPEED));
        getMoveBehaviour().setSoundFX("footstep2");
        setDieSoundFX("die1");
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BuffReceivableBehaviour(null)))));
        
        weapon = new XM1104(this);
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public <W extends Weapon> void setWeapon(Class<W> weaponClass) {
        try {
            weapon.die();
            weapon = weaponClass.getDeclaredConstructor(GameObject.class).newInstance(this);
            SceneManager.getInstance().displayLabel(weaponClass.getSimpleName(), weapon.getHitBox().getFill(), 2.5, this);
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
        hpBar.progressProperty().bind(Bindings.divide(getHp(), 100));
        hpBar.setPrefWidth(40);
        hpBar.getStyleClass().add("hp-bar");
        
        getStatusG().getChildren().add(hpBar);
//        hpBar.setLayoutY(getTranslateY() - 1);
        
        setHitBox(new Circle(15));
    }
    @Override
    public void updateUnique(long now) {
        if (effectState != null) effectState.update(now);
    }
}
