/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import static javax.swing.text.html.HTML.Tag.HEAD;
import madballs.buffState.WeaponBuff;
import madballs.collision.BuffReceivableBehaviour;
import madballs.collision.GetWeaponBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.VulnerableBehaviour;
import madballs.buffState.BuffState;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Minigun;
import madballs.wearables.Pistol;
import madballs.wearables.Uzi;
import madballs.wearables.Weapon;
/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private final int SPEED = 100;
    private BuffState buffState;
    private HBox buffBar;
    private Map<String, Circle> buffIndicators = new HashMap<>();

    public BuffState getBuffState() {
        return buffState;
    }

    public void setBuffState(BuffState effectState) {
        this.buffState = effectState;
    }
    
    public void addEffectState(BuffState buffState) {
        registerBuffState(buffState);
        buffState.wrapBuffState(this.buffState);
        this.buffState = buffState;
    }

    public void registerBuffState(BuffState buffState){
        Circle circle = new Circle(3, buffState.getColor());
        buffBar.getChildren().add(circle);
        buffIndicators.put(buffState.toString(), circle);
        if (buffState.getWrappedBuffState() != null){
            registerBuffState(buffState.getWrappedBuffState());
        }
    }

    public void removeBuffState(BuffState buffState){
        Circle circle = buffIndicators.get(buffState.toString());
        buffBar.getChildren().remove(circle);
        buffIndicators.remove(buffState.toString());
    }


    public Ball(Environment environment, double x, double y) {
        super(environment, x , y, true);
        setMoveBehaviour(new StraightMove(this, SPEED));
        getMoveBehaviour().setSoundFX("footstep2");
        setDieSoundFX("die1");
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BuffReceivableBehaviour(null)))));
  
        setWeapon(Pistol.class);
    }
    
    public Weapon getWeapon() {
        return weapon;
    }

    public <W extends Weapon> void setWeapon(Class<W> weaponClass) {
        try {
            if (weapon != null) {
                System.out.println("old weap: " + weapon.getID());
                weapon.die();
            }
            weapon = weaponClass.getDeclaredConstructor(GameObject.class).newInstance(this);
            SceneManager.getInstance().displayLabel(weaponClass.getSimpleName(), weapon.getHitBox().getFill(), 2.5, this, 0);
            if (buffState != null) buffState.reApply(WeaponBuff.class);
            if (this == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()){
                SceneManager.getInstance().setZoomOut(weapon.getScope());
                SceneManager.getInstance().bindWeaponInfo(this);
            }
            System.out.println("Get weapon " + weaponClass);
            System.out.println("new weap: " + weapon.getID());
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
        hpBar.progressProperty().bind(Bindings.divide(getHp(), 100));
        hpBar.setPrefWidth(40);
        hpBar.getStyleClass().add("hp-bar");

        buffBar = new HBox(1);
        buffBar.setTranslateY(5);
        
        getStatusG().getChildren().addAll(hpBar, buffBar);
//        hpBar.setLayoutY(getTranslateY() - 1);
        
        setHitBox(new Circle(15));
    }
    @Override
    public void updateUnique(long now) {
        if (buffState != null) {
            buffState.update(now);
        }
    }
}
