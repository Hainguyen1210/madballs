/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.LinkedList;
import madballs.multiplayer.StateData;

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
    
    public void update(){
        GameObjState newState = new GameObjState(gameObject);
        if (MadBalls.isHost()){
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
        if (gameObject.getOwner() == null){
            gameObject.setTranslateX(state.getTranslateX());
            gameObject.setTranslateY(state.getTranslateY());
            gameObject.setOldX(state.getOldX());
            gameObject.setOldY(state.getOldY());
            gameObject.setRotate(state.getDirection());
            gameObject.setOldDirection(state.getOldDirection());
        }
    }
    
}
