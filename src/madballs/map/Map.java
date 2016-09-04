/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import madballs.Common;
import madballs.ImageGenerator;
import madballs.MadBalls;
import madballs.Obstacle;
import madballs.scenes.Navigation;

import javax.imageio.ImageIO;

/**
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
    private int numTeams = 0;
    private String[][] MAP_ARRAY;
    private ArrayList<SpawnLocation> itemSpawnLocations = new ArrayList<>();
    private ArrayList<SpawnLocation> playerSpawnLocations = new ArrayList<>();
    private ArrayList<SpawnLocation> flagSpawnLocations = new ArrayList<>();
    private final static ArrayList<String> MAP_FILES = new ArrayList<>();
    private Random random = new Random();
    private int mapNumber = -1;

    public ArrayList<SpawnLocation> getFlagSpawnLocations() {
        return flagSpawnLocations;
    }

    public int getNumTeams() {
        return numTeams;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public static ArrayList<String> getMapFiles() {
        return MAP_FILES;
    }

    public static void searchFiles() {

        System.out.println("Reading map from assets/map:");
        File folder = new File("assets/map/");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                MAP_FILES.add(file.getName());
            }
            System.out.println(file.getName());
        }
        System.out.println("Read all files from assets/map");
    }


    public Image getObstacleImg() {
        return obstacleImg;
    }

    //    public Map(double length, double height, String [][] mapArray){
//        LENGTH = length;
//        HEIGHT = height;
//        MAP_ARRAY = mapArray;
//    }

    public Map(int mapNumber) {
        this.mapNumber = mapNumber;
//        LENGTH = length;
//        HEIGHT = height;
        MAP_ARRAY = loadMap();
    }

    public Map(String mapFileName) {
        this.mapNumber = MAP_FILES.indexOf(mapFileName);
        MAP_ARRAY = loadMap();
    }

    private String[][] loadMap() {
        String[][] generatedMap = null;
        //get map from file
        try {
            System.out.print("start reading file|");
            if (mapNumber == -1) {
                mapNumber = random.nextInt(MAP_FILES.size());
            }
            System.out.print("Choose Map number " + mapNumber + " |");
            Scanner mapFile = new Scanner(new File("assets/map/" + MAP_FILES.get(mapNumber)));
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

            for (int i = 0; i < numRows; i++) {
                String line = mapFile.nextLine();
                line = line.replace(".", "");

                String[] characterString = line.split("");
                // collecting info
                for (int j = 0; j < characterString.length; j++) {
                    int charCode = (int)(characterString[j].toCharArray()[0]) - 96;
                    if (characterString[j].equals("s")) {
                        itemSpawnLocations.add(new SpawnLocation(j * columnWidth, counter * rowHeight, "item", 0));
                    } else if (Common.isNumeric(characterString[j])) {
                        int teamNum = Integer.parseInt(characterString[j]);
                        if (teamNum > numTeams) numTeams = teamNum;
                        playerSpawnLocations.add(new SpawnLocation(j * columnWidth, counter * rowHeight, "ball", teamNum));
                    }
                    else if (charCode > 0 && charCode < 13){
                        flagSpawnLocations.add(new SpawnLocation(j * columnWidth, counter * rowHeight, "flag", charCode));
                    }
                }
                generatedMap[counter] = characterString;
                counter++;

            }

            System.out.println("complete reading file");
            System.out.println("Item spawn location: ");
            for (SpawnLocation sl : itemSpawnLocations) {
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

    public SpawnLocation getPlayerSpawnLocation(int teamNum) {
        ArrayList<SpawnLocation> copiedSpawnLocations = (ArrayList<SpawnLocation>) playerSpawnLocations.clone();
        for (SpawnLocation location : copiedSpawnLocations) {
            if (location.getTypeNumber() == teamNum) {
                playerSpawnLocations.remove(location);
                return location;
            }
        }
        return null;
    }

    public static Map chooseMap() {
        ArrayList<String> mapFileList = new ArrayList<>();
        for (String mapFile : Map.getMapFiles()) {
            mapFileList.add(mapFile.replace(".txt", ""));
        }
        String mapFile = Navigation.getInstance().getTextChoice("Map chooser", "Create map", "Choose the map", "random", mapFileList);
        Map map;
        if (mapFile.equals("random")) {
            map = new Map(-1);
        } else {
            map = new Map(mapFile + ".txt");
        }
        if (map.getPlayerSpawnLocations().size() < MadBalls.getMultiplayerHandler().getPlayers().size()) {
            Navigation.getInstance().showAlert("Create map", "Error", "The chosen map cannot affort current number of connected players", true);
            return chooseMap();
        } else {
            return map;
        }
    }

    public ArrayList<SpawnLocation> getItemSpawnLocations() {
        return itemSpawnLocations;
    }

    public ArrayList<SpawnLocation> getPlayerSpawnLocations() {
        return playerSpawnLocations;
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
