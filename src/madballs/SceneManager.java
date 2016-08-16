/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author caval
 */
public class SceneManager {
    public static final int numMapParts = 3;
    private static SceneManager instance = new SceneManager();
    private Rectangle2D primaryScreenBounds;
    private double screenWidth, screenHeight;
    private DoubleProperty scale = new SimpleDoubleProperty(1);
    private DoubleProperty zoomOut = new SimpleDoubleProperty(1);
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
        return scale.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public double getZoomOut() {
        return zoomOut.get();
    }

    public void setZoomOut(double zoomOut) {
        this.zoomOut.set(zoomOut);
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
        scale.bind(Bindings.divide(MadBalls.getScene().getHeight() / MadBalls.getMainEnvironment().getMap().getHeight() * numMapParts, zoomOut));
        System.out.println(MadBalls.getScene().getHeight());
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        camera.translateZProperty().bind(Bindings.multiply(zoomOut, -MadBalls.getMainEnvironment().getMap().getHeight() / numMapParts / Math.tan(Math.toRadians(30))));
        camera.setFieldOfView(30);
        camera.translateXProperty().bind(obj.getTranslateXProperty());
        camera.translateYProperty().bind(obj.getTranslateYProperty());
        MadBalls.getScene().setCamera(camera);
    }

    public void displayLabel(String labelName, Paint color, double duration, GameObject target){
        Label label = new Label(labelName);
        target.getEnvironment().getDisplay().getChildren().add(label);
        label.setTranslateZ(100);
        label.setTextFill(color);
        label.translateXProperty().bind(Bindings.add(target.getTranslateXProperty(), -labelName.length()*4));
        DoubleProperty yDiffProperty = new SimpleDoubleProperty(-20);
        label.translateYProperty().bind(Bindings.add(target.getTranslateYProperty(), yDiffProperty));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                target.getEnvironment().getDisplay().getChildren().remove(label);
                            }
                        },
                        new KeyValue(yDiffProperty, -50)));
        timeline.play();
    }
}
