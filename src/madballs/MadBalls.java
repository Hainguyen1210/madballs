/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        gameEnvironment = new Environment(root);
        
//        Client.initClient();
        Server.initServer();
        
        Scene scene = new Scene(root, 600, 500);
        
//        scene.setOnKeyPressed(thisPlayer.ball.getMoveBehaviour().keyHandler);
//        scene.setOnKeyReleased(thisPlayer.ball.getMoveBehaviour().keyHandler);
        
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
