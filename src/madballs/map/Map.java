/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import madballs.Common;

/**
 *
 * @author Caval
 */
public class Map {
    private double LENGTH;
    private double HEIGHT;
    private int [][] MAP_ARRAY;
    private ArrayList<SpawnLocation> playerSpawnLocations = new ArrayList<>();
    private final static String[] MAP_FILES = {"1.txt", "2.txt"};
    private Random random = new Random();
    private int mapNumber = -1;
    
    public int getMapNumber(){
        return mapNumber;
    }
    
    public Map(double length, double height, int [][] mapArray){
        LENGTH = length;
        HEIGHT = height;
        MAP_ARRAY = mapArray;
    }
    
    public Map(double length, double height, int mapNumber){
        this.mapNumber = mapNumber;
        LENGTH = length;
        HEIGHT = height;
        MAP_ARRAY = loadMap();
    }

    private int[][] loadMap(){
      int[][] generatedMap = new int[30][30];
      //get map from file
      try {
  System.out.println("start reading file");
        if (mapNumber == -1){
            mapNumber = random.nextInt(MAP_FILES.length);
        }
  System.out.println("Choose Map number " + mapNumber);
        Scanner mapFile = new Scanner(new File(MAP_FILES[mapNumber]));
  System.out.println("Map found.");
        int counter = 0;
        while (mapFile.hasNextLine()) {
          String line = mapFile.nextLine();
          line = line.replace(".", "");
          String[] characterString = line.split("");
          int [] charactersINT = new int [30];
          
          for(int i = 0; i < characterString.length; i++){
            if(characterString[i].equals("+")) {
              charactersINT[i] = 1;
            }else if(characterString[i].equals("-")) {
              charactersINT[i] = 0;
            }else if(Common.isNumeric(characterString[i])) {
              charactersINT[i] = -1;
              playerSpawnLocations.add(new SpawnLocation(i * LENGTH/30, counter * HEIGHT/20, "player", Integer.parseInt(characterString[i])));
            }else { continue; } //avoid hidden characters 
          }
          for(int i:charactersINT){
          }
          generatedMap[counter] = charactersINT;
          counter++;
        }
        System.out.println("complete reading file");
      } catch (FileNotFoundException ex) {
        System.out.println("Map File not Found!");
        Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
      }
      return generatedMap;
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
    
    public SpawnLocation getPlayerSpawnLocation(int teamNum){
        ArrayList<SpawnLocation> copiedSpawnLocations = (ArrayList<SpawnLocation>)playerSpawnLocations.clone();
        for (SpawnLocation location : copiedSpawnLocations){
            if (location.getTypeNumber() == teamNum) {
                playerSpawnLocations.remove(location);
                return location;
            }
        }
        return null;
    }
}
