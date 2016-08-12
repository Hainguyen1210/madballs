/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.LinkedList;
import madballs.multiplayer.StateData;
import madballs.wearables.Weapon;

/**
 *
 * @author caval
 */
public class StateLoader {
    private final LinkedList<GameObjState> serverStates = new LinkedList<>();
    private final LinkedList<GameObjState> localStates = new LinkedList<>();
    private final GameObject gameObject;
    
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
//        System.out.println("is host" + MadBalls.isHost());
        if (MadBalls.isHost() && (now - MadBalls.getGameEnvironment().getLastUpdateTime()) < 1000000000/120){
//            System.out.println(MadBalls.getGameEnvironment().getNumObjects());
            MadBalls.getMultiplayerHandler().sendData(new StateData(newState));
        }
        else {
            synchronized (serverStates){
                synchronized(localStates){
                    addLocalState(newState);
                    GameObjState localState, serverState;
                    while (serverStates.size() > 0){
                        localState = localStates.size() > 0 ? localStates.getFirst() : null;
                        serverState = serverStates.getFirst();
                        if (localState == null || localState.getUpdateIndex() > serverState.getUpdateIndex()){
                            loadState(serverState);
                            serverStates.remove();
                        }
                        else if (localState.getUpdateIndex() == serverState.getUpdateIndex()){
                            loadState(serverState);
                            localStates.remove();
                            serverStates.remove();
                        }
                        else if (localState.getUpdateIndex() < serverState.getUpdateIndex()){
                            localStates.remove();
                        }
                    }
                }
            }
        }
        
    }
    
    public void loadState(GameObjState state){
        if (state.isDead()){
//            System.out.println("`");
            gameObject.setDead();
            MadBalls.getGameEnvironment().removeGameObj(gameObject);
            return;
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
                rotateBehaviour.setTargetX(state.getTargetX());
                rotateBehaviour.setTargetY(state.getTargetY());
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
