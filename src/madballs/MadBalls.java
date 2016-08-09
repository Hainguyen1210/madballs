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
import madballs.map.Map;
import madballs.multiplayer.Client;
import madballs.multiplayer.MultiplayerHandler;
import madballs.multiplayer.Server;

/**
 *
 * @author Caval
 */
public class MadBalls extends Application {
    public static final double RESOLUTION_X = 1280;
    public static final double RESOLUTION_Y = 720;
    
    private static Environment gameEnvironment;
    private static Navigation navigation;
    private static MultiplayerHandler multiplayerHandler;
    
    private static Scene scene;
    
    public static Scene getScene(){
        return scene;
    }

    public static Environment getGameEnvironment() {
        return gameEnvironment;
    }
    
    public static MultiplayerHandler getMultiplayerHandler(){
        return multiplayerHandler;
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        navigation = new Navigation();
        Pane root = new Pane();
        
        scene = new Scene(root, RESOLUTION_X, RESOLUTION_Y);
        gameEnvironment = new Environment(root);
//        Client.initClient();
        
        
        boolean isHost = navigation.getConfirmation("", "Start game", "Do you want to host?");
        if (isHost){
            multiplayerHandler = new Server();
            Map map = new Map(RESOLUTION_X, RESOLUTION_Y, -1);
            gameEnvironment.loadMap(map);
        }
        else {
            multiplayerHandler = new Client();
        }
        multiplayerHandler.init();
        
        primaryStage.setTitle("MAD BALL");
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
