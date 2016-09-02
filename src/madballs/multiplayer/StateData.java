/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import madballs.GameObjState;

/**
 *
 * @author caval
 */
public class StateData extends Data{
    private GameObjState state;

    public GameObjState getState(){
        return state;
    }
    
    public StateData(GameObjState state) {
        super("state");
        this.state = state;
    }
    
}
