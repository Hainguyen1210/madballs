package madballs.scenes.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import madballs.AI.BotPlayer;
import madballs.ImageGenerator;
import madballs.MadBalls;
import madballs.gameMode.NormalMode;
import madballs.item.Spawner;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.multiplayer.Data;
import madballs.multiplayer.PlayerData;
import madballs.multiplayer.Server;
import madballs.multiplayer.SpawnData;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.ScenesFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by caval on 24/08/2016.
 */
public class GameRoomController implements Initializable {
    @FXML
    private AnchorPane playersPane;
    @FXML
    private Button startBtn, addBotBtn;
    @FXML
    private ListView<String> weaponChoiceList;
    @FXML
    private TextArea sceneHeight;
    @FXML
    private ImageView mapPreview;

    private GridPane playersGrid = new GridPane();
    private ObservableList<Integer> teams = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            if (MadBalls.isHost()) {
                startBtn.setVisible(true);
                addBotBtn.setVisible(true);
                weaponChoiceList.setVisible(true);
                ArrayList<String> weaponNames = new ArrayList<>();
                for (Class weaponClass: Spawner.getWeapons()){
                    weaponNames.add(weaponClass.getSimpleName());
                }
                ObservableList<String> weaponList = FXCollections.observableArrayList(weaponNames);
                weaponChoiceList.setItems(weaponList);
            }
            sceneHeight.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        MadBalls.setSceneHeight(Integer.parseInt(newValue));
                    } catch (NumberFormatException ex){
                        MadBalls.setSceneHeight(720);
                    }
                }
            });
            playersPane.getChildren().add(playersGrid);
            ArrayList<Player> players = MadBalls.getMultiplayerHandler().getPlayers();
            for (Player player : players){
                displayPlayer(player);
            }

            mapPreview.setImage(ImageGenerator.getInstance().getImage("map_" + Map.getMapFiles().get(MadBalls.getMainEnvironment().getMap().getMapNumber()).replace(".txt", "")));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void startGame(ActionEvent e){
        if (MadBalls.getGameMode() instanceof NormalMode){
            int weaponChoiceIndex = weaponChoiceList.getSelectionModel().getSelectedIndex();
            if (weaponChoiceIndex >= 0){
                ((NormalMode)MadBalls.getGameMode()).setWeaponClassIndex(weaponChoiceIndex);
            }
        }

        int ranking = 1;
        for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
            if (player.getTeamNum() == 0){
                Navigation.getInstance().showAlert("Start game", "Error", "Each player must have a team", true);
                return;
            }
            player.setRanking(ranking++);
            MadBalls.getMultiplayerHandler().sendData(new PlayerData(player));
        }
        MadBalls.getMultiplayerHandler().startMatch();
    }

    @FXML
    public void addBot(ActionEvent e){
        Server server = (Server) MadBalls.getMultiplayerHandler();
        server.addBotPlayer();
    }

    public void displayPlayer(Player player){
        Label nameLabel = new Label(player.getName());
        Pane playerDisplay = new Pane(nameLabel);
        updateTeamChoices();
        if (player == MadBalls.getMultiplayerHandler().getLocalPlayer() || (MadBalls.isHost() && player instanceof BotPlayer)){
            ChoiceBox choiceBox = new ChoiceBox(teams);
            choiceBox.setTranslateX(200);
            choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (newValue == null) {
                        return;
                    }
                    player.setTeamNum((Integer) newValue);
                    MadBalls.getMultiplayerHandler().sendData(new PlayerData(player));
                }
            });
            playerDisplay.getChildren().add(choiceBox);
        }
        Label teamLabel = new Label();
        teamLabel.textProperty().bind(Bindings.format("%d", player.teamNumProperty()));
        teamLabel.setTranslateX(150);
        playerDisplay.getChildren().add(teamLabel);
        playerDisplay.setPrefSize(350, 50);


        int i = MadBalls.getMultiplayerHandler().getPlayers().indexOf(player);
        playersGrid.add(playerDisplay, i % 2, i / 2);
    }

    public void updateTeamChoices(){
        teams.clear();
        ArrayList<Integer> tempTeams;
        for (SpawnLocation spawnLocation: MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocations()){
            teams.add(spawnLocation.getTypeNumber());
        }
        tempTeams = new ArrayList<>(teams);
        for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
            for (Integer teamNum : tempTeams){
                if (player.getTeamNum() == teamNum){
                    teams.remove(teamNum);
                    break;
                }
            }
            tempTeams = new ArrayList<>(teams);
        }
        System.out.println("update team choices");
    }

    public void updatePlayersPane(){
        playersPane.getChildren().clear();
        playersGrid = new GridPane();
        playersPane.getChildren().add(playersGrid);
        for (Player player : MadBalls.getMultiplayerHandler().getPlayers()){
            displayPlayer(player);
        }
    }
}
