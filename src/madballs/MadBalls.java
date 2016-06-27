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
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static Player nextPlayer;
    
    @Override
    public void start(Stage primaryStage) {
        
        
        Pane root = new Pane();
        Environment environment = new Environment(root);
        Player thisPlayer = new Player();
        thisPlayer.generateBall(environment, 100, 100);
        nextPlayer = new Player();
        nextPlayer.generateBall(environment, 200, 200);
        
        Client.initClient();
//        Server.initServer();
        
        Scene scene = new Scene(root, 600, 500);
        
        scene.setOnKeyPressed(thisPlayer.ball.getMoveBehaviour().keyHandler);
        scene.setOnKeyReleased(thisPlayer.ball.getMoveBehaviour().keyHandler);
        
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
