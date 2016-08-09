/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import madballs.map.SpawnLocation;

/**
 *
 * @author caval
 */
public class SpawnData extends Data{
    private double x, y;
    private int typeNumber;
    private String spawnType;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTypeNum() {
        return typeNumber;
    }
    
    public String getSpawntype(){
        return spawnType;
    }
    
    public SpawnData(SpawnLocation spawnLocation) {
        super("spawn");
        this.x = spawnLocation.getX();
        this.y = spawnLocation.getY();
        this.spawnType = spawnLocation.getType();
        this.typeNumber = spawnLocation.getTypeNumber();
    }
    
}
