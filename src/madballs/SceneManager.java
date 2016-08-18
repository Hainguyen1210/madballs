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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import madballs.buffState.BuffState;

/**
 *
 * @author caval
 */
public class SceneManager {
    public static final int numMapParts = 3;
    private static SceneManager instance = new SceneManager();
    private Stage gameInfoStage;
    private FlowPane gameInfoDisplay;
    private ProgressBar hpBar = new ProgressBar();
    private Label weaponLabel = new Label();
    private Label buffLabel = new Label();
    private Rectangle2D primaryScreenBounds;
    private double screenWidth, screenHeight;
    // scale: the ratio of the visual/scene size to the actual game element size.
    // i.e. multiplying an element's size by this scale would give the visual size of the element (the size on scene)
    private DoubleProperty scale = new SimpleDoubleProperty(1);
    // zoomOut: the ratio of how much the game elements have been zoomed out compared to its initial size
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

    public Stage getGameInfoStage() {
        return gameInfoStage;
    }

    public Pane getGameInfoDisplay() {
        return gameInfoDisplay;
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
    
    public void displayGameInfo(Group root){


        hpBar.setPrefSize(250, 25);
        hpBar.setTranslateX(50);
        weaponLabel.setTranslateX(120);
        buffLabel.setTranslateX(140);

        gameInfoDisplay = new FlowPane(hpBar, weaponLabel, buffLabel);
        gameInfoDisplay.setAlignment(Pos.CENTER_LEFT);
        gameInfoDisplay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        gameInfoDisplay.setPrefWidth(MadBalls.getMainScene().getWidth());
        gameInfoDisplay.setPrefHeight(30);
        gameInfoDisplay.translateYProperty().bind(Bindings.subtract(MadBalls.getMainScene().getHeight(), gameInfoDisplay.heightProperty()));
//        gameInfoDisplay.setTranslateY(MadBalls.getMainScene().getHeight() - 30);
        gameInfoDisplay.setTranslateZ(-1);

        root.getChildren().add(gameInfoDisplay);
    }
    
    public void bindCamera(GameObject obj){
        System.out.println("asd" + MadBalls.getAnimationScene().getHeight());
        scale.bind(Bindings.divide(MadBalls.getAnimationScene().getHeight() / MadBalls.getMainEnvironment().getMap().getHeight() * numMapParts, zoomOut));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        // display the map by how many vertical parts it has been divided into and how much it has been zoom out
        // e.g. if the map is 720px high, divided into 3 parts, and zoomed out by 1.5, the displaying height is 720/3*1.5 = 360
        camera.translateZProperty().bind(Bindings.multiply(zoomOut, -MadBalls.getMainEnvironment().getMap().getHeight() / numMapParts / Math.tan(Math.toRadians(30))));
        camera.setFieldOfView(30);
        camera.translateXProperty().bind(obj.getTranslateXProperty());
        camera.translateYProperty().bind(obj.getTranslateYProperty());
        MadBalls.getMainEnvironment().getDisplay().getChildren().add(camera);
        MadBalls.getAnimationScene().setCamera(camera);
    }

    public void bindWeaponInfo(Ball ball){
        if (ball.getWeapon().getAmmo() >= 0){
            weaponLabel.textProperty().bind(Bindings.format("%s / %d", ball.getWeapon().getClass().getSimpleName(), ball.getWeapon().ammoProperty()));
        }
        else {
            weaponLabel.textProperty().bind(Bindings.format("%s / *", ball.getWeapon().getClass().getSimpleName()));
        }

    }

    public void bindBallInfo(Ball ball){
//        gameInfoDisplay.translateXProperty().bind(Bindings.subtract(ball.getTranslateXProperty(), Bindings.divide(MadBalls.getAnimationScene().getWidth()/2, scale)));
//        gameInfoDisplay.translateYProperty().bind(Bindings.add(ball.getTranslateYProperty(),
//                Bindings.subtract(Bindings.divide(MadBalls.getAnimationScene().getHeight()/2, scale),
//                        Bindings.multiply(50, scale))));


        hpBar.progressProperty().bind(Bindings.divide(ball.getHp(), 100));
    }

    public void displayLabel(String labelName, Paint color, double duration, GameObject target, double delay){
        Label label = new Label(labelName);
        label.setTranslateZ(100);
        label.setTextFill(color);
        label.translateXProperty().bind(Bindings.add(target.getTranslateXProperty(), -labelName.length()*4));
        DoubleProperty yDiffProperty = new SimpleDoubleProperty(-20);
        label.translateYProperty().bind(Bindings.add(target.getTranslateYProperty(), yDiffProperty));

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                target.getEnvironment().getDisplay().getChildren().remove(label);
                            }
                        },
                        new KeyValue(yDiffProperty, -50))
        );

        if (delay > 0){
            final Timeline delayTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(delay),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    target.getEnvironment().getDisplay().getChildren().add(label);
                                    timeline.play();
                                }
                            })
            );
            delayTimeline.play();
        }
        else {
            target.getEnvironment().getDisplay().getChildren().add(label);
            timeline.play();
        }

    }

    public void updateBuffStatus(BuffState state){
        if (state == null){
            buffLabel.setText("");
            return;
        }
        String stateString = state.getClass().getSimpleName();
        BuffState wrappedState = state.getWrappedBuffState();
        while (wrappedState != null){
            stateString += ", " + wrappedState.getClass().getSimpleName();
            wrappedState = wrappedState.getWrappedBuffState();
        }
        buffLabel.setText(stateString);
    }

    public void bindBall(Ball ball){
        bindCamera(ball);
        bindBallInfo(ball);
        bindWeaponInfo(ball);
    }
}
