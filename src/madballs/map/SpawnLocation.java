/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.map;

import java.io.Serializable;

/**
 *
 * @author caval
 */
public class SpawnLocation implements Serializable{
    private double x;
    private double y;
    private String type;
    private int typeNumber;


    private boolean isSpawned = false;

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public double getX() { return x; }

    public double getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    public SpawnLocation(double x, double y, String type, int typeNumber) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.typeNumber = typeNumber;
    }
    
    
}
