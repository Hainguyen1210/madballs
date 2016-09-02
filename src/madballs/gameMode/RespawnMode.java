package madballs.gameMode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import madballs.Ball;
import madballs.MadBalls;
import madballs.item.Spawner;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.BannerData;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;

import java.util.ArrayList;

/**
 * Created by caval on 02/09/2016.
 */
public class RespawnMode extends NormalMode {
    private int respawnTime, winningKillsCount;
    private ArrayList<Integer> respawningIDs = new ArrayList<>();

    public int getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public RespawnMode(int weaponClassIndex, int respawnTime) {
        super(1);
        setWeaponClassIndex(weaponClassIndex);
        this.respawnTime = respawnTime;
//        try {
//            this.winningKillsCount = Integer.parseInt(Navigation.getInstance().getTextResponse("New Game", "Respawn Game Mode", "Enter the winning number of kills", "30"));
//        }
//        catch (NumberFormatException ex){
//            this.winningKillsCount = 30;
//        }
        this.winningKillsCount = 30;
    }

    @Override
    public void manage(long now) {
        ArrayList<Player> players = MadBalls.getMultiplayerHandler().getPlayers();
        for (Player player: players){
            Ball ball = player.getBall();
            if (ball.isDead()){
                if (!respawningIDs.contains(ball.getID())){
                    if (player.isLocal()){
                        SceneManager.getInstance().displayCountdown(respawnTime);
                    }
                    else {
                        player.sendData(new BannerData("", respawnTime));
                    }
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(respawnTime), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ball.setHpValue(100);
                            StraightMove straightMove = (StraightMove) ball.getMoveBehaviour();
                            straightMove.setVelocityX(0);
                            straightMove.setVelocityY(0);
                            ball.setTranslateX(player.getSpawnLocation().getX());
                            ball.setTranslateY(player.getSpawnLocation().getY());
                            ball.setOldX(ball.getTranslateX());
                            ball.setOldY(ball.getTranslateY());
                            ball.setRotate(0);
                            ball.getEnvironment().resurrectGameObj(ball.getID());
                            ball.getStateLoader().update(now);

                            int weaponClassIndex = getWeaponClassIndex();
                            if (weaponClassIndex < 0) weaponClassIndex = 0;
                            ball.setWeapon(Spawner.getWeapons()[weaponClassIndex], -1);
                        }
                    }));
                    timeline.play();
                    respawningIDs.add(ball.getID());
                }
            }
            else {
                if (respawningIDs.contains(ball.getID())){
                    respawningIDs.remove(ball.getID());
                }
            }
        }
    }

    @Override
    public void checkWinner(long now) {
        if (MadBalls.isGameOver()){
            return;
        }

        for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
            if (player.getKillsCount() >= winningKillsCount){
                MadBalls.setGameOver(true);
                SceneManager.getInstance().getScoreBoardContainer().setVisible(true);
                if (player.isLocal()){
                    Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
                }
                else {
                    Navigation.getInstance().showAlert("Game over", "You lose!", "Better luck next time.", false);
                }
                if (MadBalls.isHost()){
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(8), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            MadBalls.getMultiplayerHandler().newMatch(true);
                        }
                    }));
                    timeline.play();
                }
                return;
            }
        }
    }
}
