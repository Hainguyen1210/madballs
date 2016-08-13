/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author caval
 */
public class ScenesManager {
    public static final double RESOLUTION_X = 800;
    public static final double RESOLUTION_Y = 800;
    
    private static ScenesManager instance = new ScenesManager();
    private double screenWidth, screenHeight;
    private double scaleX, scaleY;
    private PerspectiveCamera camera;
    
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
    
    public void scaleDisplay(){
//        Environment.getInstance().getDisplay().setScaleX(scaleX);
//        Environment.getInstance().getDisplay().setScaleY(scaleY);
    }
    
    public void setCamera(GameObject obj){
//        camera = new PerspectiveCamera(true);
//        camera.setNearClip(0.1);
//        camera.setFarClip(1000);
//        camera.setTranslateZ(-1000);
//        camera.setFieldOfView(30);
//        camera.translateXProperty().bind(obj.getTranslateXProperty());
//        camera.translateYProperty().bind(obj.getTranslateYProperty());
//        MadBalls.getScene().setCamera(camera);
    }
}
