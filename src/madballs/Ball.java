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
import madballs.buffState.BuffState;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.*;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private final int SPEED = 100;
    private BuffState buffState;

    public BuffState getBuffState() {
        return buffState;
    }

    public void setBuffState(BuffState effectState) {
        this.buffState = effectState;
    }
    
    public void addEffectState(BuffState buffState) {
        buffState.wrapBuffState(this.buffState);
        this.buffState = buffState;
    }


    public Ball(Environment environment, double x, double y) {
        super(environment, x , y, true);
        setMoveBehaviour(new StraightMove(this, SPEED));
        getMoveBehaviour().setSoundFX("footstep2");
        setDieSoundFX("die1");
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BuffReceivableBehaviour(null)))));
        
        weapon = new Awp(this);
        SceneManager.getInstance().setZoomOut(weapon.getScope());
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public <W extends Weapon> void setWeapon(Class<W> weaponClass) {
        try {
            weapon.die();
            weapon = weaponClass.getDeclaredConstructor(GameObject.class).newInstance(this);
            SceneManager.getInstance().setZoomOut(weapon.getScope());
            SceneManager.getInstance().displayLabel(weaponClass.getSimpleName(), weapon.getHitBox().getFill(), 2.5, this, 0);
            if (this == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()){
                SceneManager.getInstance().bindWeaponInfo(this);
            }
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
        if (buffState != null) {
            buffState.update(now);
            if (this == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()){
//                System.out.println(buffState.getWrappedBuffState() == null);
                SceneManager.getInstance().updateBuffStatus(buffState);
            }
        }
    }
}
