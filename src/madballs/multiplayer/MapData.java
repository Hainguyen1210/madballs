/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 *
 * @author caval
 */
public class MapData extends Data{
    private int mapNumber;
    
    public int getMapNumber(){
        return mapNumber;
    }
    
    public MapData(int mapNumber) {
        super("choose_map");
        this.mapNumber = mapNumber;
    }
    
}
