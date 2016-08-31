package madballs.scenes.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import madballs.map.Map;
import madballs.multiplayer.Client;
import madballs.multiplayer.Server;

import java.net.URL;
import java.util.ResourceBundle;

import madballs.MadBalls;
import madballs.player.Player;

/**
 * Created by caval on 31/08/2016.
 */
public class WelcomeController implements Initializable {
    @FXML
    private TextArea hostIPArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void hostGame(){
        MadBalls.setMultiplayerHandler(new Server());
        MadBalls.loadMap(Map.chooseMap());
        MadBalls.getMultiplayerHandler().init();
    }

    @FXML
    public void joinGame(){
        MadBalls.setMultiplayerHandler(new Client(hostIPArea.getText()));
        MadBalls.getMultiplayerHandler().setLocalPlayer(new Player(null, true));
        MadBalls.getMultiplayerHandler().init();
    }
}
