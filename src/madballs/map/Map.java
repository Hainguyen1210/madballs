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

import javafx.scene.image.Image;
import madballs.Common;
import madballs.ImageGenerator;

/**
 *
 * @author Caval
 */
public class Map {
    private Image obstacleImg = ImageGenerator.getInstance().getImage("obstacle/plain");
    private double width, height;
    private int numRows;
    private int numColumns;
    private int columnWidth;
    private int rowHeight;
    private int obstacleSize;
    private String [][] MAP_ARRAY;
    private ArrayList<SpawnLocation> itemSpawnLocations = new ArrayList<>();
    private ArrayList<SpawnLocation> playerSpawnLocations = new ArrayList<>();
    private final static String[] MAP_FILES = {"1.txt", "2.txt"};
    private Random random = new Random();
    private int mapNumber = -1;

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getMapNumber(){
        return mapNumber;
    }

    public Image getObstacleImg() {
        return obstacleImg;
    }

    //    public Map(double length, double height, String [][] mapArray){
//        LENGTH = length;
//        HEIGHT = height;
//        MAP_ARRAY = mapArray;
//    }
    
    public Map(int mapNumber){
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
        Scanner mapFile = new Scanner(new File("assets/map/" + MAP_FILES[mapNumber]));
        System.out.print("Map found.|");
        int counter = 0;
        System.out.println("Analyzing Map.|");
        
        // collecting Map preset
        String presetLine = mapFile.nextLine();
        String[] presetString = presetLine.split(" ");
for (String s : presetString) System.out.println(" " + s);
        this.numRows = Integer.parseInt(presetString[1]);
        this.numColumns = Integer.parseInt(presetString[2]);
        this.rowHeight = Integer.parseInt(presetString[3]);
        this.columnWidth = Integer.parseInt(presetString[4]);
        this.obstacleSize = Integer.parseInt(presetString[5]);
        height = rowHeight * numRows;
        width = columnWidth * numColumns;
        generatedMap = new String[numRows][numColumns];

        for(int i = 0; i< numRows; i++) {
          String line = mapFile.nextLine();
          line = line.replace(".", "");

            String[] characterString = line.split("");
            // collecting info
            for(int j = 0; j < characterString.length; j++){
              if(characterString[j].equals("s")){
                itemSpawnLocations.add(new SpawnLocation(j * columnWidth, counter * rowHeight, "item", 0));
              }else if(Common.isNumeric(characterString[j])) {
                playerSpawnLocations.add(new SpawnLocation(j * columnWidth, counter * rowHeight, "player", Integer.parseInt(characterString[j])));
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
    
  public int getColumnWidth() {
    return columnWidth;
  }

  public int getRowHeight() {
    return rowHeight;
  }
  
  public int getObstacleSize() {
    return obstacleSize;
  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumColumns() {
    return numColumns;
  }
  
}
