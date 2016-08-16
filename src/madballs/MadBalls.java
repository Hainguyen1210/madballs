/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import madballs.gameFX.SoundStudio;
import madballs.map.Map;
import madballs.multiplayer.Client;
import madballs.multiplayer.MultiplayerHandler;
import madballs.multiplayer.Server;

/**
 *
 * @author Caval
 */
public class MadBalls extends Application {
    private static Navigation navigation;
    private static MultiplayerHandler multiplayerHandler;
    private static Environment mainEnvironment;
    private static boolean isGameOver = false;

    private static Scene scene;

    public static Environment getMainEnvironment() {
        return mainEnvironment;
    }

    public static Scene getScene(){
        return scene;
    }
    
    public static MultiplayerHandler getMultiplayerHandler(){
        return multiplayerHandler;
    }

    public static Navigation getNavigation() {
        return navigation;
    }
    
    public static boolean isHost(){
        return multiplayerHandler.isHost();
    }

    public static boolean isGameOver() {
        return isGameOver;
    }

    public static void setGameOver(boolean isGameOver) {
        MadBalls.isGameOver = isGameOver;
    }
    
    @Override
    public void start(Stage primaryStage) {
//        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        SoundStudio.getInstance();
        
        navigation = new Navigation();
        Group root = new Group();
        
        scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        mainEnvironment = new Environment();
        mainEnvironment.setDisplay(root);
//        Client.initClient();
        
        
        boolean isHost = navigation.getConfirmation("", "Start game", "Do you want to host?");
        if (isHost){
            multiplayerHandler = new Server();
            Map map = new Map(-1);
            mainEnvironment.loadMap(map);
        }
        else {
            multiplayerHandler = new Client();
        }
        multiplayerHandler.init();
        
        primaryStage.setTitle("MAD BALL");
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneManager.getInstance().displayGameInfo(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
