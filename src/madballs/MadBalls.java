/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import madballs.Map.Map;

/**
 *
 * @author Caval
 */
public class MadBalls extends Application {
    public static final double RESOLUTION_X = 1280;
    public static final double RESOLUTION_Y = 720;
    
    private static Environment gameEnvironment;

    public static Environment getGameEnvironment() {
        return gameEnvironment;
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        
        Pane root = new Pane();
        
        Map map = new Map(RESOLUTION_X, RESOLUTION_Y);
        
        gameEnvironment = new Environment(root, map);
        
//        Client.initClient();
//        Server.initServer();
        
        Scene scene = new Scene(root, RESOLUTION_X, RESOLUTION_Y);
        
        Ball ball = new Ball(gameEnvironment, 80, 80);
        Ball enemyBall = new Ball(gameEnvironment, 150, 80);
        
        
        scene.setOnKeyPressed(ball.getMoveBehaviour().keyHandler);
        scene.setOnKeyReleased(ball.getMoveBehaviour().keyHandler);
        scene.setOnMousePressed(ball.getWeapon().getMoveBehaviour().mouseHandler);
        scene.setOnMouseReleased(ball.getWeapon().getMoveBehaviour().mouseHandler);
        scene.setOnMouseMoved(ball.getWeapon().getMoveBehaviour().mouseHandler);
        scene.setOnMouseDragged(ball.getWeapon().getMoveBehaviour().mouseHandler);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
