/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.scenes;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Caval
 * this class handles the navigation of the application
 */
public class Navigation {
    private static Navigation instance = new Navigation();
    private Stage mainStage;
    private Scene[] history = new Scene[0];

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private Navigation(){

    }

    public static Navigation getInstance(){
        return instance;
    }
    
    public Scene[] getHistory(){
        return history;
    }
    
    // add the new location to the history
    public void addHistory(Scene location){
        int newHistoryLength = history.length + 1;
        Scene[] newHistory = new Scene[newHistoryLength];
        for (int i = 0; i < newHistoryLength; i++){
            if (i != newHistoryLength - 1){
                newHistory[i] = history[i];
            }
            else {
                newHistory[i] = location;
            }
        }
        history = newHistory;
    }
    
    // replace current scene of the mainStage with new scene
    public void navigate(Scene destination){
        // load the new scene into the stage
        mainStage.setScene(destination);

        // set the stage at the middle of the screen (src: http://stackoverflow.com/questions/29558449/javafx-center-stage-on-screen)
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX((primScreenBounds.getWidth() - mainStage.getWidth()) / 2);
        mainStage.setY((primScreenBounds.getHeight() - mainStage.getHeight()) / 2);

        // update the navigation history
        addHistory(destination);
    }
    
    public void showInterupt(String title, String header, String content, boolean shouldWait){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
//        alert.setOnCloseRequest((DialogEvent e) -> {
//            e.consume();
//        });
        
        if (shouldWait) {
            alert.showAndWait();
        }
        else {
            alert.show();
        }
    }

    public Alert createInterupt(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

//        alert.setOnCloseRequest((DialogEvent e) -> {
//            e.consume();
//        });

        return alert;
    }
    
    // show a modal alert box
    public void showAlert(String title, String header, String content, boolean shouldWait) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
       if (shouldWait) {
            alert.showAndWait();
        }
        else {
            alert.show();
        }
    }
    
    /**
     * show a confirmation modal pane and return the user response as 'OK' or 'Cancel'
     * @param title
     * @param header
     * @param content
     * @return 
     */
    public boolean getConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }

    public String getTextChoice(String title, String header, String content, String defaultChoice, List<String> list){
        ChoiceDialog<String> dialog = new ChoiceDialog<>(defaultChoice, list);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.get();
    }
    
    /**
     * show a modal pane that get the user response as text
     * @param title
     * @param header
     * @param content
     * @param defaultResponse
     * @return 
     */
    public String getTextResponse(String title, String header, String content, String defaultResponse) {
        TextInputDialog dialog = new TextInputDialog(defaultResponse);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        
        Optional<String> response = dialog.showAndWait();
        if (response.isPresent()) {
            return response.get();
        }
        else {
            return "none";
        }
    }
    
    // navigate to the previous scene in the history
    public void back(){
        Scene[] tempHistory = new Scene[history.length - 1];
        System.arraycopy(history, 0, tempHistory, 0, history.length - 1);
        history = tempHistory;
//        navigate(history[history.length - 1]);
    }
}

