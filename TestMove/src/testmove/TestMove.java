/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testmove;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import testmove.MultiplePressedKeysEventHandler.MultiKeyEvent;
import testmove.MultiplePressedKeysEventHandler.MultiKeyEventHandler;

/**
 *
 * @author Caval
 */
public class TestMove extends Application {
    public static double scaleX = 1;
    public static double scaleY = 1;
    private Shape hbox = new Rectangle(50, 50);
    private static Pane root = new Pane();
    final double speed = 100 ; // pixels per second
    final double minX = 0 ;
    final double minY = 0 ;
    final DoubleProperty velocityX = new SimpleDoubleProperty();
    final DoubleProperty velocityY = new SimpleDoubleProperty();
    final LongProperty lastUpdateTime = new SimpleLongProperty();
    final LongProperty lastShot = new SimpleLongProperty();
    final LongProperty lastObstacle = new SimpleLongProperty();
    final LongProperty lastAttack = new SimpleLongProperty();
    final BooleanProperty isLeftMousePressed = new SimpleBooleanProperty(false);
    final DoubleProperty mouseX = new SimpleDoubleProperty();
    final DoubleProperty mouseY = new SimpleDoubleProperty();
    private ArrayList<Shape> obstacles = new ArrayList<>();
    
    final AnimationTimer animation = new AnimationTimer() {
      @Override
      public void handle(long timestamp) {
        if (lastUpdateTime.get() > 0) {
            double maxX = 550;
            final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
            final double deltaX = elapsedSeconds * velocityX.get();
            final double oldX = hbox.getTranslateX();
            final double newX = Math.max(minX, Math.min(maxX, oldX + deltaX));
            hbox.setTranslateX(newX);
            
            double maxY = 450;
            final double deltaY = elapsedSeconds * velocityY.get();
            final double oldY = hbox.getTranslateY();
            final double newY = Math.max(minY, Math.min(maxY, oldY + deltaY));
            hbox.setTranslateY(newY);
            
//            System.out.println(hbox.getWidth());
        }
        lastUpdateTime.set(timestamp);
      }
    };
    
    final AnimationTimer spawnObstacles = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastObstacle.get() > 2000000000 ){
                Random rand = new Random(); 
                double destX = -1, destY = -1;
                while (!((destX >= 0 && destX < 150) || (destX >= 430 && destX < 580))){
                    destX = rand.nextInt(550);
                }
                while (!((destY >= 0 && destY < 100) || (destY >= 380 && destX < 480))){
                    destY = rand.nextInt(450);
                }
//                System.out.println("destX: " + destX);
//                System.out.println("destY: " + destY);
                Shape obstacle = new Rectangle(20, 20);
                obstacle.setFill(Paint.valueOf("green"));
                obstacle.setTranslateX(destX);
                obstacle.setTranslateY(destY);
                root.getChildren().add(obstacle);
                obstacles.add(obstacle);
                lastObstacle.set(now);
            }
        }
    };
    
    final AnimationTimer spawnBullets = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastShot.get() > 1000000000 && isLeftMousePressed.get()) {
                System.out.println("fire");
                System.out.println(mouseX.get());
                new Bullet(mouseX.get(), mouseY.get(), hbox.getTranslateX() + 25, hbox.getTranslateY() + 25);
                lastShot.set(now);
            }
        }
    };
    
    final AnimationTimer attack = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastAttack.get() > 1000000000){
                for (Shape obstacle : obstacles){
                    Double angle = Math.atan2(hbox.getTranslateY() + 15 - obstacle.getTranslateY(), hbox.getTranslateX() + 15 - obstacle.getTranslateX());
                    Double moveX = Math.cos(angle) * 50;
                    Double moveY = Math.sin(angle) * 50;
                    obstacle.setTranslateX(obstacle.getTranslateX() + moveX);
                    obstacle.setTranslateY(obstacle.getTranslateY() + moveY);
                    
                    Shape intersect = Shape.intersect(obstacle, hbox);
                    if (intersect.getBoundsInLocal().getWidth() != -1 ){
                        root.getChildren().remove(hbox);
                        spawnBullets.stop();
                    }
                    lastAttack.set(now);
                }
            }
        }
    };
    
    final MultiplePressedKeysEventHandler keyHandler = 
        new MultiplePressedKeysEventHandler(new MultiKeyEventHandler() {

            public void handle(MultiKeyEvent ke) {
                if (!((ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) && (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)))) {
                    velocityX.set(0);
                }
                if (!((ke.isPressed(KeyCode.UP)  || ke.isPressed(KeyCode.W)) && (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)))) {
                    velocityY.set(0);
                }
                
                
                if (ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) {
                    velocityX.set(-speed);
                }
                if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)) {
                    velocityX.set(speed);
                }

                if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.W)) {
                    velocityY.set(-speed);
                }
                if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)) {
                    velocityY.set(speed);
                }
            }
        });
    
    @Override
    public void start(Stage primaryStage) {
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        Scene scene = new Scene(root, 600, 500);
        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(this, scene, 6/5, 500, 600, root);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
        
        hbox.setFill(Paint.valueOf("red"));
        hbox.setTranslateX(275);
        hbox.setTranslateY(225);
        
        final Circle circle = new Circle(10, Paint.valueOf("yellow"));
        root.getChildren().add(hbox);
        
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);
        
        scene.setCursor(Cursor.CROSSHAIR);
        
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX.set(event.getSceneX() / scaleX);
                mouseY.set(event.getSceneY() / scaleY);
                System.out.println("dragged");
                System.out.println(mouseX);
            }
        });
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX.set(event.getSceneX() / scaleX);
                mouseY.set(event.getSceneY() / scaleY);
//                new Bullet(event.getSceneX(), event.getSceneY(), hbox.getTranslateX() + 25, hbox.getTranslateY() + 25);
                isLeftMousePressed.set(true);
            }
        });
        
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isLeftMousePressed.set(false);
            }
        });
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        
        primaryStage.setFullScreen(true);
        primaryStage.show();

        animation.start();
        spawnBullets.start();
        spawnObstacles.start();
        attack.start();
//        root.requestFocus();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private static class SceneSizeChangeListener implements ChangeListener<Number> {
        private final Application app;
        private final Scene scene;
        private final double ratio;
        private final double initHeight;
        private final double initWidth;
        private final Pane contentPane;

        public SceneSizeChangeListener(Application app, Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
            this.app = app;
            this.scene = scene;
            this.ratio = ratio;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
            this.contentPane = contentPane;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            final double newWidth  = contentPane.getWidth();
            final double newHeight = contentPane.getHeight();

            Scale scale = new Scale(newWidth /  initWidth, newHeight / initHeight);
            scale.setPivotX(0);
            scale.setPivotY(0);
            scene.getRoot().getTransforms().setAll(scale);
            
            TestMove.scaleX = newWidth/initWidth;
            TestMove.scaleY = newHeight/initHeight;
//            contentPane.setPrefWidth (newWidth);
//            contentPane.setPrefHeight(newHeight);
        }
    }
    
    public class Bullet{
        private Circle display;
        
        public Bullet(double targetX, double targetY, double initX, double initY){
            System.out.println("target");
            System.out.println(targetX);
            display = new Circle(initX, initY, 20, Paint.valueOf("yellow"));
            TestMove.root.getChildren().add(display);
            final LongProperty lastUpdateTime = new SimpleLongProperty();
            final double angle = Math.atan2(targetY - initY, targetX - initX);
            final double moveX = Math.cos(angle) * 500;
            final double moveY = Math.sin(angle) * 500;
            
            AnimationTimer projectile = new AnimationTimer() {
                @Override
                public void handle(long timestamp) {
                    if (lastUpdateTime.get() > 1000000000) {
                        double maxX = 580;
                        final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
                        final double deltaX = elapsedSeconds * moveX;
                        final double oldX = display.getTranslateX();
                        final double newX = Math.min(maxX, oldX + deltaX);
                        display.setTranslateX(newX);

                        double maxY = 480;
                        final double deltaY = elapsedSeconds * moveY;
                        final double oldY = display.getTranslateY();
                        final double newY = Math.min(maxY, oldY + deltaY);
                        display.setTranslateY(newY);
                        
                        for (Shape obstacle : obstacles){
                            Shape intersect = Shape.intersect(obstacle, display);
                            if (intersect.getBoundsInLocal().getWidth() != -1) {
                                root.getChildren().remove(obstacle);
                            }
                        }
                    }
                    lastUpdateTime.set(timestamp);
                }
            };
            
            projectile.start();
        }
    }
}
