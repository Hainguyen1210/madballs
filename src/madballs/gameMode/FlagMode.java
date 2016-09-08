package madballs.gameMode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import madballs.Flag;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.multiplayer.BindingData;
import madballs.multiplayer.SpawnData;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;

import java.util.ArrayList;

/**
 * Created by caval on 03/09/2016.
 */
public class FlagMode extends RespawnMode {
    private ArrayList<Flag> flags = new ArrayList<>();

    public FlagMode(int weaponClassIndex, int respawnTime) {
        super(weaponClassIndex, respawnTime);
        setMode(2);
    }

    @Override
    public void organize() {
        super.organize();
        if (MadBalls.isHost()) {
            for (SpawnLocation spawnLocation: MadBalls.getMainEnvironment().getMap().getFlagSpawnLocations()){
                Flag flag = new Flag(MadBalls.getMainEnvironment(), -1, spawnLocation);
                System.out.println("flag " + flag.getID());
                flags.add(flag);
                MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false, flag.getID()));
            }
        }
    }

    @Override
    public void checkWinner(long now){
        if (MadBalls.isHost()){
            if (MadBalls.isGameOver()){
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(8), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        MadBalls.getMultiplayerHandler().newMatch(true);
                    }
                }));
                timeline.play();
                return;
            }
            Map map = MadBalls.getMainEnvironment().getMap();
            for (Flag flag: flags){
                if (flag.getCarrierID() != -1){
                    GameObject carrier = flag.getEnvironment().getObject(flag.getCarrierID());
                    int carrierRow = (int) (carrier.getTranslateY() / map.getRowHeight());
                    int carrierColumn = (int) (carrier.getTranslateX() / map.getColumnWidth());

                    for (SpawnLocation spawnLocation: map.getFlagSpawnLocations()){
                        if (flag.getTeamNum() == spawnLocation.getTypeNumber()) continue;
                        int baseRow = (int) (spawnLocation.getY() / map.getRowHeight()) + 1;
                        int baseColumn = (int) (spawnLocation.getX() / map.getColumnWidth());

                        int teamNum = spawnLocation.getTypeNumber();
                        if (carrierRow == baseRow && carrierColumn == baseColumn) {
                            SceneManager.getInstance().addScore(teamNum, 1);
                            SceneManager.getInstance().announceFlag(flag.getCarrierID(), flag.getID(), "CAPTURED");
                            flag.unbindDisplay();
                            MadBalls.getMultiplayerHandler().sendData(new BindingData(-1, flag.getID(), -1, -1));
                            flag.setTranslateX(flag.getSpawnLocation().getX());
                            flag.setTranslateY(flag.getSpawnLocation().getY());
                            flag.setRotate(0);
                            flag.setCarrierID(-1);
                            flag.getStateLoader().update(now);
                        }
                    }
                }
            }
        }
        else {
            if (MadBalls.isGameOver()) return;
        }

        for (Integer teamNum: SceneManager.getInstance().getTeamScoreBoard().keySet()){
            if (SceneManager.getInstance().getTeamScoreBoard().get(teamNum).get() >= 5){
                MadBalls.setGameOver(true);
                SceneManager.getInstance().getScoreBoardContainer().setVisible(true);
                if (MadBalls.getMultiplayerHandler().getLocalPlayer().getTeamNum() == teamNum){
                    Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
                }
                else {
                    Navigation.getInstance().showAlert("Game over", "You lose!", "Better luck next time.", false);
                }
                return;
            }
        }
    }

    @Override
    public void updateKill(Player killer, Player victim) {

    }
}
