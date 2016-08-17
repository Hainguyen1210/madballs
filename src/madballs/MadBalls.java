/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
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

    private static Scene mainScene;
    private static SubScene animationScene;

    public static Scene getMainScene() {
        return mainScene;
    }

    public static Environment getMainEnvironment() {
        return mainEnvironment;
    }

    public static SubScene getAnimationScene(){
        return animationScene;
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
        animationScene = new SubScene(root, 1280, 720);

        Group mainRoot = new Group(animationScene);

        mainScene = new Scene(mainRoot, 1280, 720, true, SceneAntialiasing.BALANCED);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        mainEnvironment = new Environment();
        mainEnvironment.setDisplay(root);
//        Client.initClient();
        
        
        boolean isHost = navigation.getConfirmation("", "Start game", "Do you want to host?");
        if (isHost){
            multiplayerHandler = new Server();
            Map map = new Map(-1);
            mainEnvironment.loadMap(map);
            mainEnvironment.startAnimation();
        }
        else {
            multiplayerHandler = new Client();
        }
        multiplayerHandler.init();
        
        primaryStage.setTitle("MAD BALL");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        SceneManager.getInstance().displayGameInfo(mainRoot);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
