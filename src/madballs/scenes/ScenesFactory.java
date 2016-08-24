/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.scenes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import madballs.Environment;

/**
 * this class takes care of the Scenes of the app
 * @author Caval
 */
public class ScenesFactory {
    private static ScenesFactory instance = new ScenesFactory();
    private FXMLLoader fxmlLoader = new FXMLLoader();

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    private ScenesFactory(){

    }

    public static ScenesFactory getInstance(){
        return instance;
    }
    
    /**
     * create and return a new Scene by loading from the fxml file
     * @param sceneName
     * @return
     * @throws IOException 
     */
    public Scene newScene(String sceneName){
        Scene scene = null;
        try {
            if (sceneName.equals("prepare")){
                fxmlLoader = new FXMLLoader(getClass().getResource("fxml/gameRoom.fxml"));
            }
            else if (sceneName.equals("hof")){
                fxmlLoader = new FXMLLoader(getClass().getResource("fxml/hallOfFame.fxml"));
            }
            else if (sceneName.equals("game")){

            }
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        }
        catch (IOException ex){
            System.err.println("Cannot load scene");
        }
        return scene;
    }
}
