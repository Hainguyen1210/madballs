/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.LinkedList;

import madballs.moveBehaviour.MoveBehaviour;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.StateData;
import madballs.projectiles.Projectile;
import madballs.wearables.Weapon;

/**
 *
 * @author caval
 */
public class StateLoader {
    private final LinkedList<GameObjState> serverStates = new LinkedList<>();
    private final LinkedList<GameObjState> localStates = new LinkedList<>();
    private final GameObject gameObject;
    private long lastLoadTime = 0;
    
    public StateLoader(GameObject obj){
        gameObject = obj;
    }
    
    public void addServerState(GameObjState state){
        synchronized(serverStates){
            if (serverStates.size() == 10){
                serverStates.remove();
            }
            serverStates.add(state);
        }
    }
    
    public void addLocalState(GameObjState state){
        synchronized(localStates){
            if (localStates.size() == 10){
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
            MadBalls.getMultiplayerHandler().sendData(new StateData(newState));
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
                            if (localState.isSimilarTo(serverState)){
                                isReconcilated = true;
                                localStates.remove();
                            }
                            else {
                                loadState(serverState);
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

    public void loadState(GameObjState state){
//        if (state.isDead() && gameObject instanceof Weapon) {
//            System.out.println("dead state" + gameObject.getID());
//            System.out.println(gameObject.getClass());
//        }
//        if (gameObject.isDead() && !state.isDead()){
//            gameObject.getDisplay().setVisible(true);
//        }
        if (state.isDead()){
//            System.out.println("dead");
            gameObject.setDead();
        }
        gameObject.setHpValue(state.getHp());
        if (gameObject.getOwner() == null){
            gameObject.setTranslateX(state.getTranslateX());
            gameObject.setTranslateY(state.getTranslateY());
            gameObject.setOldX(state.getOldX());
            gameObject.setOldY(state.getOldY());
            gameObject.setRotate(state.getDirection());
            gameObject.setOldDirection(state.getOldDirection());
        }
        if (gameObject.getMoveBehaviour() != null){
            MoveBehaviour moveBehaviour = gameObject.getMoveBehaviour();
            if (gameObject.getMoveBehaviour() instanceof RotateBehaviour){
                RotateBehaviour rotateBehaviour = (RotateBehaviour) moveBehaviour;
                rotateBehaviour.setNewDirection(state.getDirection());
//                rotateBehaviour.setTargetX(state.getTargetX());
//                rotateBehaviour.setTargetY(state.getTargetY());
            }
            else if (gameObject.getMoveBehaviour() instanceof StraightMove){
                StraightMove straightMove = (StraightMove)gameObject.getMoveBehaviour();
                straightMove.setVelocityX(state.getVelocityX());
                straightMove.setVelocityY(state.getVelocityY());
            }
            moveBehaviour.setSpeed(state.getSpeed());
        }
        if (gameObject instanceof Weapon){
            Weapon weapon = (Weapon) gameObject;
            weapon.setDamage(state.getDamage());
            weapon.setFireRate(state.getFireRate());
        }
    }

}
