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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import madballs.buffState.WeaponBuff;
import madballs.collision.BuffReceivableBehaviour;
import madballs.collision.GetWeaponBehaviour;
import madballs.collision.PushBackEffect;
import madballs.collision.PushableBehaviour;
import madballs.collision.VulnerableBehaviour;
import madballs.buffState.BuffState;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.GetWeaponData;
import madballs.player.Player;
import madballs.scenes.SceneManager;
import madballs.wearables.*;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Image ballImage;
    private Weapon weapon;
    private final int SPEED = 100;
    private BuffState buffState;
    private HBox buffBar;
    private Map<String, Circle> buffIndicators = new HashMap<>();
    private Player player;

    public Player getPlayer() {
        return player;
    }

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


    public Ball(Environment environment, double x, double y, Integer id, Player pLayer) {
        super(environment, x , y, true, id);
        this.player = pLayer;
        setMoveBehaviour(new StraightMove(this, SPEED));
        getMoveBehaviour().setSoundFX("footstep2");
        setDieSoundFX("die1");
        setCollisionEffect(new PushBackEffect(null, -1));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new VulnerableBehaviour(new PushableBehaviour(new BuffReceivableBehaviour(null)))));

        weapon = new GrenadeLauncher(this, -1);
        SceneManager.getInstance().setZoomOut(weapon.getScope());
    }

    public Image getBallImage() {
        return ballImage;
    }

    public void setBallImage(Image ballImage) {
        this.ballImage = ballImage;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public <W extends Weapon> void setWeapon(Class<W> weaponClass, Integer weaponID) {
        try {
            StraightMove straightMove = (StraightMove) getMoveBehaviour();
            if (weapon != null) {
                straightMove.setSpeed(straightMove.getSpeed() + weapon.getWeight() * 5);
//                System.out.println("old weap: " + weapon.getID());
                weapon.die();
            }
            weapon = weaponClass.getDeclaredConstructor(GameObject.class, Integer.class).newInstance(this, weaponID);
            straightMove.setSpeed(straightMove.getSpeed() - weapon.getWeight() * 5);
            if (MadBalls.isHost()) {
                MadBalls.getMultiplayerHandler().sendData(new GetWeaponData(getID(), weapon.getClass().getName(), weapon.getID()));
            }
            SceneManager.getInstance().displayLabel(weaponClass.getSimpleName(), weapon.getHitBox().getFill(), 2.5, this, 0);
            if (buffState != null) buffState.reApply(WeaponBuff.class);
            if (this == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()){
                SceneManager.getInstance().setZoomOut(weapon.getScope());
                SceneManager.getInstance().bindWeaponInfo(this);
            }
//            System.out.println("Get weapon " + weaponClass);
//            System.out.println("new weap: " + weapon.getID());
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
        buffBar.setTranslateY(10);
        
        getStatusG().getChildren().addAll(hpBar, buffBar);
//        hpBar.setLayoutY(getTranslateY() - 1);
        
        setHitBox(new Circle(15));
        getImageView().setEffect(new DropShadow(10, Color.BLACK));
    }
    @Override
    public void updateUnique(long now) {
        if (buffState != null) {
            buffState.update(now);
        }
    }
}
