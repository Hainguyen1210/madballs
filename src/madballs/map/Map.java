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
    private int MapLenghtX;
    private int MapLenghtY;
    private int box_X;
    private int box_Y;
    private int boxSize;
    private String [][] MAP_ARRAY;
    private ArrayList<SpawnLocation> itemSpawnLocations = new ArrayList<>();
    private ArrayList<SpawnLocation> playerSpawnLocations = new ArrayList<>();
    private final static String[] MAP_FILES = {"1.txt"};
    private Random random = new Random();
    private int mapNumber = -1;
    
    public int getMapNumber(){
        return mapNumber;
    }
    
//    public Map(double length, double height, String [][] mapArray){
//        LENGTH = length;
//        HEIGHT = height;
//        MAP_ARRAY = mapArray;
//    }
    
    public Map(double length, double height, int mapNumber){
        this.mapNumber = mapNumber;
//        LENGTH = length;
//        HEIGHT = height;
        MAP_ARRAY = loadMap();
    }

    private String[][] loadMap(){
      String [][] generatedMap = null;
      //get map from file
      try {
        System.out.print("start reading file|");
        if (mapNumber == -1){
            mapNumber = random.nextInt(MAP_FILES.length);
        }
        System.out.print("Choose Map number " + mapNumber + " |");
        Scanner mapFile = new Scanner(new File(MAP_FILES[mapNumber]));
        System.out.print("Map found.|");
        int counter = 0;
        System.out.println("Analyzing Map.|");
        
        // collecting Map preset
        String presetLine = mapFile.nextLine();
        String[] presetString = presetLine.split(" ");
for (String s : presetString) System.out.println(" " + s);
        this.MapLenghtX = Integer.parseInt(presetString[1]);
        this.MapLenghtY = Integer.parseInt(presetString[2]);
        this.box_X = Integer.parseInt(presetString[3]);
        this.box_Y = Integer.parseInt(presetString[4]);
        this.boxSize = Integer.parseInt(presetString[5]);
        generatedMap = new String[MapLenghtX][MapLenghtY];

        for(int i=0; i<MapLenghtX; i++) {
          String line = mapFile.nextLine();
          line = line.replace(".", "");

            String[] characterString = line.split("");
            // collecting info
            for(int j = 0; j < characterString.length; j++){
              if(characterString[j].equals("s")){
                itemSpawnLocations.add(new SpawnLocation(j * box_X, counter * box_Y, "item", 0));
              }else if(Common.isNumeric(characterString[j])) {
                playerSpawnLocations.add(new SpawnLocation(j * box_X, counter * box_Y, "player", Integer.parseInt(characterString[j])));
              }
            }
            generatedMap[counter] = characterString;
            counter++;            
          
        }
        
        System.out.println("complete reading file");
        System.out.println("Item spawn location: ");
        for(SpawnLocation sl : itemSpawnLocations){
          System.out.println(sl.getX() + " " + sl.getY());
        }
      } catch (FileNotFoundException ex) {
        System.out.println("Map File not Found!");
        Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
      }
      return generatedMap;
    }


    public String[][] getMAP_ARRAY() {
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

    public ArrayList<SpawnLocation> getItemSpawnLocations() {
      return itemSpawnLocations;
    }
    
  public int getBox_X() {
    return box_X;
  }

  public int getBox_Y() {
    return box_Y;
  }
  
  public int getBoxSize() {
    return boxSize;
  }

  public int getMapLenghtX() {
    return MapLenghtX;
  }

  public int getMapLenghtY() {
    return MapLenghtY;
  }
  
}
