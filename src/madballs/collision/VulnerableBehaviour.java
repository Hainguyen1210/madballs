/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.gameFX.SoundStudio;
import madballs.multiplayer.PlayerData;
import madballs.player.Player;

/**
 *
 * @author Caval
 */
public class VulnerableBehaviour extends StackedCollisionPassiveBehaviour{

    public VulnerableBehaviour(CollisionPassiveBehaviour behaviour) {
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
                    Ball sourceBall = (Ball)source.getEnvironment().getObject(damageEffect.getBallID());
                    Ball targetBall = (Ball) target;
                    int newKill =  sourceBall.getPlayer().getTeamNum() == targetBall.getPlayer().getTeamNum() ? -1 : 1;
                    sourceBall.getPlayer().setKillsCount(sourceBall.getPlayer().getKillsCount() + newKill);
                    targetBall.getPlayer().setDeathsCount(targetBall.getPlayer().getDeathsCount() + 1);
                    sourceBall.getPlayer().updateRanking();
                    for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                        System.out.println("ranked" + player.getName() + player.getRanking());
                    }
                    MadBalls.getMultiplayerHandler().sendData(new PlayerData(sourceBall.getPlayer()));
                    MadBalls.getMultiplayerHandler().sendData(new PlayerData(targetBall.getPlayer()));
                }
            }
        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof DamageEffect;
    }

}
