/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Map;

/**
 *
 * @author Caval
 */
public class Map {
    private double LENGTH;
    private double HEIGHT;
    private int [][] MAP_ARRAY;
    
    public Map(double length, double height, int [][] mapArray){
        LENGTH = length;
        HEIGHT = height;
        MAP_ARRAY = mapArray;
    }

    public double getLENGTH() {
        return LENGTH;
    }

    public double getHEIGHT() {
        return HEIGHT;
    }

    public int[][] getMAP_ARRAY() {
        return MAP_ARRAY;
    }
}
