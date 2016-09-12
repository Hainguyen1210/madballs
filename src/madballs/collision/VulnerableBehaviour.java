/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.Explosion;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.gameFX.SoundStudio;
import madballs.multiplayer.PlayerData;
import madballs.player.Player;
import madballs.projectiles.Projectile;
import madballs.scenes.SceneManager;
import madballs.wearables.Weapon;

/**
 * can be damaged by DamageEffect
 * @author Caval
 */
public class VulnerableBehaviour extends StackedCollisionPassiveBehaviour{

    public VulnerableBehaviour(StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        SoundStudio.getInstance().playAudio("penetrate", target.getTranslateX(), target.getTranslateY(), 500, 500);
        DamageEffect damageEffect = (DamageEffect) effect;
        target.setHpValue(target.getHpValue() - damageEffect.getDamage());
//        System.out.println(target.getHpValue());
        if (target.getHpValue() <= 0){
            target.setHpValue(0);
            target.die();
            if (MadBalls.isHost()){
                if (target instanceof Ball && damageEffect.getBallID() >= 0){
                    Ball sourceBall = (Ball)(source.getEnvironment().getObject(damageEffect.getBallID()));
                    Ball targetBall = (Ball) target;
                    if (sourceBall == null){
                        sourceBall = (Ball) source.getEnvironment().getDeadObject(damageEffect.getBallID());
                    }
                    int newKill =  sourceBall.getPlayer().getTeamNum() == targetBall.getPlayer().getTeamNum() ? -1 : 1;
                    sourceBall.getPlayer().setKillsCount(sourceBall.getPlayer().getKillsCount() + newKill);
                    targetBall.getPlayer().setDeathsCount(targetBall.getPlayer().getDeathsCount() + 1);
                    String weaponImageName = "pistol";
                    if (source instanceof Projectile){
                        weaponImageName = ((Projectile)source).getSourceWeapon().getImageName();
                    }
                    else if (source instanceof Explosion) {
                        Weapon sourceWeapon = (Weapon) source.getEnvironment().getObject(((Explosion) source).getWeaponID());
                        if (sourceWeapon == null){
                            sourceWeapon = (Weapon) source.getEnvironment().getDeadObject(((Explosion) source).getWeaponID());
                        }
                        weaponImageName = sourceWeapon.getImageName();
                    }
                    else if (source instanceof Ball){
                        weaponImageName = "smashed the head of";
                    }
                    SceneManager.getInstance().announceKill(sourceBall.getID(), targetBall.getID(), weaponImageName);
                    sourceBall.getPlayer().updateRanking();
                    for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                        MadBalls.getMultiplayerHandler().sendData(new PlayerData(player));
                    }
                    MadBalls.getGameMode().updateKill(sourceBall.getPlayer(), targetBall.getPlayer());
//                    for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
//                        System.out.println("ranked" + player.getName() + player.getRanking());
//                    }
                }
            }
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof DamageEffect && !target.isDead();
    }

}
