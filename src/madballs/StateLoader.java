/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import madballs.AI.BotPlayer;
import madballs.moveBehaviour.MoveBehaviour;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.StateData;
import madballs.player.Player;
import madballs.projectiles.Projectile;
import madballs.wearables.Weapon;

/**
 *
 * @author caval
 */
public class StateLoader {
    private final Map<Player, GameObjState> playerStateMap = new HashMap<>();
    private final LinkedList<GameObjState> serverStates = new LinkedList<>();
    private final LinkedList<GameObjState> localStates = new LinkedList<>();
    private final GameObject gameObject;
    private long lastLoadTime = 0;
    
    public StateLoader(GameObject obj){
        gameObject = obj;
    }
    
    public void addServerState(GameObjState state){
        synchronized(serverStates){
            if (serverStates.size() == 5){
                serverStates.remove();
            }
            serverStates.add(state);
        }
    }
    
    public void addLocalState(GameObjState state){
        synchronized(localStates){
            if (localStates.size() == 5){
                localStates.remove();
            }
            localStates.add(state);
        }
    }
    
    public void update(long now){
        if (gameObject instanceof Obstacle || gameObject instanceof Ground) return;
        GameObjState newState = new GameObjState(gameObject);
        if (lastLoadTime == 0) lastLoadTime = now;
        if (MadBalls.isHost()){
            for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                if (player instanceof BotPlayer) continue;
                if (!player.getRelevancy(gameObject.getTranslateX(), gameObject.getTranslateY(), 500, 500)){
                    continue;
                }
                GameObjState lastRelevantState = playerStateMap.get(player);
                if (lastRelevantState != null) {
                    if (lastRelevantState.isSimilarTo(newState)) {
                        continue;
                    }
                    else {
                        playerStateMap.replace(player, newState);
                    }
                }
                else {
                    playerStateMap.put(player, newState);
                }
                player.sendData(new StateData(newState));
            }
        }
        else {
            synchronized (serverStates){
                synchronized(localStates){
                    addLocalState(newState);
                    GameObjState localState, serverState;
                    boolean isReconcilated = false;
                    while (serverStates.size() > 0){
                        localState = localStates.size() > 0 ? localStates.getFirst() : null;
                        serverState = serverStates.getFirst();
                        if (localState == null || localState.getUpdateIndex() > serverState.getUpdateIndex()){
                            loadState(serverState);
                            serverStates.remove();
                            isReconcilated = false;
                        }
                        else if (localState.getUpdateIndex() == serverState.getUpdateIndex()){
                            if (loadState(serverState)){
                                isReconcilated = true;
                                localStates.remove();
                            }
                            else {
                                localStates.clear();
                                isReconcilated = false;
                            }
                            serverStates.remove();
                        }
                        else if (localState.getUpdateIndex() < serverState.getUpdateIndex()){
                            localStates.remove();
                            isReconcilated = false;
                        }
                    }
                    if (isReconcilated){
                        for (GameObjState state : localStates){
                            loadState(state);
                        }
                    }
                }
            }
        }

    }

    public boolean loadState(GameObjState state){
        boolean isSimilar = true;
//        if (state.isDead() && gameObject instanceof Weapon) {
//            System.out.println("dead state" + gameObject.getID());
//            System.out.println(gameObject.getClass());
//        }
//        if (gameObject.isDead() && !state.isDead()){
//            gameObject.getDisplay().setVisible(true);
//        }
        if (state.isDead() && !gameObject.isDead()){
            isSimilar = false;
//            System.out.println("dead");
            gameObject.setDead();
        }
        if (state.getHp() != gameObject.getHpValue()){
            gameObject.setHpValue(state.getHp());
            isSimilar = false;
        }
        if (gameObject.getOwner() == null){
            if (gameObject.getTranslateX() != state.getTranslateX()) {
                gameObject.setTranslateX(state.getTranslateX());
                isSimilar = false;
            }
            if (gameObject.getTranslateY() != state.getTranslateY()) {
                gameObject.setTranslateY(state.getTranslateY());
                isSimilar = false;
            }
            if (gameObject.getOldX() != state.getOldX()) {
                gameObject.setOldX(state.getOldX());
                isSimilar = false;
            }
            if (gameObject.getOldY() != state.getOldY()) {
                gameObject.setOldY(state.getOldY());
                isSimilar = false;
            }
            if (gameObject.getRotateAngle() != Math.toDegrees(state.getDirection())) {
                gameObject.setRotate(state.getDirection());
                isSimilar = false;
            }
            if (gameObject.getOldDirection() != state.getOldDirection()) {
                gameObject.setOldDirection(state.getOldDirection());
                isSimilar = false;
            }
        }
        if (gameObject.getMoveBehaviour() != null){
            MoveBehaviour moveBehaviour = gameObject.getMoveBehaviour();
            if (gameObject.getMoveBehaviour() instanceof RotateBehaviour){
                RotateBehaviour rotateBehaviour = (RotateBehaviour) moveBehaviour;
                if (rotateBehaviour.getNewDirection() != state.getDirection()) {
                    rotateBehaviour.setNewDirection(state.getDirection());
                    isSimilar = false;
                }
//                rotateBehaviour.setTargetX(state.getTargetX());
//                rotateBehaviour.setTargetY(state.getTargetY());
            }
            else if (gameObject.getMoveBehaviour() instanceof StraightMove){
                StraightMove straightMove = (StraightMove)gameObject.getMoveBehaviour();
                if (straightMove.getVelocityX() != state.getVelocityX()){
                    straightMove.setVelocityX(state.getVelocityX());
                    isSimilar = false;
                }
                if (straightMove.getVelocityY() != state.getVelocityY()) {
                    straightMove.setVelocityY(state.getVelocityY());
                    isSimilar = false;
                }
            }
            if (moveBehaviour.getSpeed() != state.getSpeed()) {
                moveBehaviour.setSpeed(state.getSpeed());
                isSimilar = false;
            }
        }
        if (gameObject instanceof Weapon){
            Weapon weapon = (Weapon) gameObject;
            if (weapon.getDamage() != state.getDamage()) {
                weapon.setDamage(state.getDamage());
                isSimilar = false;
            }
            if (weapon.getFireRate() != weapon.getFireRate()) {
                weapon.setFireRate(state.getFireRate());
                isSimilar = false;
            }
        }

        return isSimilar;
    }

}
