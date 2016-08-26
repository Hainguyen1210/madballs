/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import madballs.gameFX.SoundStudio;
import madballs.map.Map;
import madballs.multiplayer.Client;
import madballs.multiplayer.MultiplayerHandler;
import madballs.multiplayer.Server;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;
import madballs.scenes.ScenesFactory;

/**
 *
 * @author Caval
 */
public class MadBalls extends Application {
    private static MultiplayerHandler multiplayerHandler;
    private static Environment mainEnvironment;
    private static boolean isGameOver = false;
    private static double sceneHeight = 720;

    private static Scene mainScene;
    private static SubScene animationScene;

    public static void setSceneHeight(double sceneHeight) {
        MadBalls.sceneHeight = sceneHeight;
    }

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
        Navigation.getInstance().setMainStage(primaryStage);

//        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        SoundStudio.getInstance();


//        MapGenerator.getInstance().generateMapImage(); // EXPORT MAP BACKGROUND
        Map.searchFiles();

        primaryStage.setTitle("MAD BALL");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();

        boolean isHost = Navigation.getInstance().getConfirmation("", "Start game", "Do you want to host?");
        if (isHost){
            multiplayerHandler = new Server();

            loadMap(Map.chooseMap());
        }
        else {
            multiplayerHandler = new Client();
            multiplayerHandler.setLocalPlayer(new Player(null, true));
        }
        multiplayerHandler.init();

//        Navigation.getInstance().navigate(ScenesFactory.getInstance().newScene("prepare"));
//        SceneManager.getInstance().displayGameInfo(mainRoot);
    }

    public static void restart(){
        Map map = null;
        if (mainEnvironment != null) map = new Map(mainEnvironment.getMap().getMapNumber());
        isGameOver = false;
        Group root = new Group();
        root.getChildren().add(new ImageView(ImageGenerator.getInstance().getImage("map" + map.getMapNumber())));
        animationScene = new SubScene(root, sceneHeight/9*16, sceneHeight, true, SceneAntialiasing.BALANCED);

        Group mainRoot = new Group(animationScene);

        mainScene = new Scene(mainRoot, sceneHeight/9*16, sceneHeight);
        mainScene.setFill(Color.BLACK);
        mainScene.getStylesheets().add(MadBalls.class.getResource("scenes/style.css").toExternalForm());

        mainEnvironment = new Environment();
        mainEnvironment.setDisplay(root);
        if (map != null) {
            mainEnvironment.loadMap(map);
        }
        SceneManager.getInstance().displayGameInfo(mainRoot);
    }

    public static void loadMap(Map map){
        Group root = new Group();
        if (mainEnvironment !=null) mainEnvironment.stopAnimation();
        mainEnvironment = new Environment();
        mainEnvironment.setDisplay(root);
        mainEnvironment.loadMap(map);
        SceneManager.getInstance().resetTeamScoreBoard();
        Navigation.getInstance().navigate(ScenesFactory.getInstance().newScene("prepare"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
