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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import madballs.AI.BotPlayer;
import madballs.buffState.NewBorn;
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
 * A Ball is the main character of a Player.
 * Ball can receive Buffs, and carry a Weapon
 * @author Caval
 */
public class Ball extends GameObject{
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

    /**
     * receive a new BuffState
     * @param buffState
     */
    public void addBuffState(BuffState buffState) {
        registerBuffState(buffState);
        buffState.wrapBuffState(this.buffState);
        this.buffState = buffState;
    }

    /**
     * display a new BuffState in the BuffBar
     * @param buffState
     */
    public void registerBuffState(BuffState buffState){
        Circle circle = new Circle(3, buffState.getColor());
        buffBar.getChildren().add(circle);
        buffIndicators.put(buffState.toString(), circle);
        if (buffState.getWrappedBuffState() != null){
            registerBuffState(buffState.getWrappedBuffState());
        }
    }

    /**
     * remove a BuffState from the BuffBar
     * @param buffState
     */
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
        setCollisionEffect(new PushBackEffect(-1, null));
        setCollisionPassiveBehaviour(new GetWeaponBehaviour(new PushableBehaviour(new BuffReceivableBehaviour(new VulnerableBehaviour(null)))));

        weapon = new Pistol(this, -1);
        SceneManager.getInstance().setZoomOut(weapon.getScope());
    }

    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * switch to a new Weapon with the given WeaponClass
     * @param weaponClass the Class of the new Weapon
     * @param weaponID the obj id to give to the new weapon
     * @param <W>
     */
    public <W extends Weapon> void setWeapon(Class<W> weaponClass, Integer weaponID) {
        try {
            StraightMove straightMove = (StraightMove) getMoveBehaviour();

            // destroy the old weapon
            if (weapon != null) {
                straightMove.setSpeed(straightMove.getSpeed() + weapon.getWeight() * 5);
                weapon.die();
            }

            // create and wield the new weapon
            weapon = weaponClass.getDeclaredConstructor(GameObject.class, Integer.class).newInstance(this, weaponID);
            straightMove.setSpeed(straightMove.getSpeed() - weapon.getWeight() * 5);
            if (MadBalls.isHost()) {
                MadBalls.getMultiplayerHandler().sendData(new GetWeaponData(getID(), weapon.getClass().getName(), weapon.getID()));
            }

            // reapply weapon buff
            if (buffState != null) buffState.reApply(WeaponBuff.class);

            // display the weapon info and change the view based on the scope
            SceneManager.getInstance().displayLabel(weaponClass.getSimpleName(), weapon.getHitBox().getFill(), 2.5, this, 0);
            if (this == MadBalls.getMultiplayerHandler().getLocalPlayer().getBall()){
                SceneManager.getInstance().setZoomOut(weapon.getScope());
                SceneManager.getInstance().bindWeaponInfo(this);
            }
            if (getPlayer() instanceof BotPlayer){
                getPlayer().getController().setScale(SceneManager.getInstance().getScale() * MadBalls.getMultiplayerHandler().getLocalPlayer().getBall().getWeapon().getScope() / weapon.getScope());
            }

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void revive(){
        boolean isReviving = isDead();
        super.revive();
        // apply the NewBorn state onto the Ball when it has just revived
        if (isReviving) {
            if (player.isLocal()) SceneManager.getInstance().bindCamera(this);
            BuffState buffState = new NewBorn(null, 3);
            buffState.castOn(this, 0);
            addBuffState(buffState);
        }
    }

    @Override
    public void setDead(){
        clearBuff();
        super.setDead();
    }

    public void clearBuff(){
        if (buffState != null){
            buffState.forceFade();
            clearBuff();
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
