/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Screen;

/**
 *
 * @author caval
 */
public class SceneManager {
    public static final int numMapParts = 3;
    private static SceneManager instance = new SceneManager();
    private Rectangle2D primaryScreenBounds;
    private double screenWidth, screenHeight;
    private double scale;
    private PerspectiveCamera camera;

    public Rectangle2D getPrimaryScreenBounds() {
        return primaryScreenBounds;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public double getScale() {
        return scale;
    }
    
    private SceneManager(){
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = primaryScreenBounds.getWidth();
        screenHeight = primaryScreenBounds.getHeight();
//        scaleX = screenWidth / RESOLUTION_X;
//        scaleY = screenHeight / RESOLUTION_Y;
    }
    
    public static SceneManager getInstance(){
        return instance;
    }
    
    public void scaleDisplay(Group display){

    }
    
    public void setCamera(GameObject obj){
        scale = MadBalls.getScene().getHeight() / MadBalls.getMainEnvironment().getMap().getHeight() * numMapParts;
        System.out.println(MadBalls.getScene().getHeight());
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        camera.setTranslateZ(-MadBalls.getMainEnvironment().getMap().getHeight() / numMapParts / Math.tan(Math.toRadians(30)));
        camera.setFieldOfView(30);
        camera.translateXProperty().bind(obj.getTranslateXProperty());
        camera.translateYProperty().bind(obj.getTranslateYProperty());
        MadBalls.getScene().setCamera(camera);
    }
}
