/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 *
 * @author caval
 */
public class ScenesManager {
    public static final double RESOLUTION_X = 1280;
    public static final double RESOLUTION_Y = 720;
    
    private static ScenesManager instance = new ScenesManager();
    private double screenWidth, screenHeight;
    private double scaleX, scaleY;
    
    private ScenesManager(){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = primaryScreenBounds.getWidth();
        screenHeight = primaryScreenBounds.getHeight();
        scaleX = screenWidth / RESOLUTION_X;
        scaleY = screenHeight / RESOLUTION_Y;
    }
    
    public static ScenesManager getInstance(){
        return instance;
    }
    
    public void scaleScene(){
        
    }
    
    
}
