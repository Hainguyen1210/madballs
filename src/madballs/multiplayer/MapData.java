/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import madballs.map.Map;

/**
 * the Data about the current game Map
 * @author caval
 */
public class MapData extends Data{
    private int mapNumber;
    private int gameMode;

    public int getGameMode() {
        return gameMode;
    }

    public int getMapNumber(){
        return mapNumber;
    }
    
    public MapData(Map map) {
        super("choose_map");
        this.mapNumber = map.getMapNumber();
        this.gameMode = map.getGameMode();
    }
    
}
