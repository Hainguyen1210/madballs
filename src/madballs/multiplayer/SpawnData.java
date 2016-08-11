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
    
    public SpawnData(double x, double y, String spawnType, int typeNumber) {
        super("spawn");
        this.x = x;
        this.y = y;
        this.spawnType = spawnType;
        this.typeNumber = typeNumber;
    }
    
    public SpawnData(SpawnLocation spawnLocation, boolean isLocal) {
        super("spawn");
        this.x = spawnLocation.getX();
        this.y = spawnLocation.getY();
        this.spawnType = spawnLocation.getType() + (isLocal ? "_local" : "");
        this.typeNumber = spawnLocation.getTypeNumber();
    }
}
