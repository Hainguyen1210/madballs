package madballs.gameMode;

import madballs.MadBalls;
import madballs.item.Spawner;
import madballs.multiplayer.WinnerData;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;

import java.util.ArrayList;

/**
 * Created by caval on 31/08/2016.
 */
public class NormalMode extends GameMode {
    private int weaponClassIndex = 0;

    public void setWeaponClassIndex(int weaponClassIndex) {
        this.weaponClassIndex = weaponClassIndex;
    }

    public int getWeaponClassIndex() {
        return weaponClassIndex;
    }

    public NormalMode(int weaponClassIndex) {
        super(0);
        this.weaponClassIndex = weaponClassIndex;
    }

    @Override
    public void organize() {
        if (MadBalls.isHost() && weaponClassIndex >= 0){
            for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                player.getBall().setWeapon(Spawner.getWeapons()[weaponClassIndex], -1);
            }
        }
    }

    @Override
    public void manage(long now) {

    }

    @Override
    public void checkWinner(long now){
        ArrayList<Player> players = MadBalls.getMultiplayerHandler().getPlayers();
        if (MadBalls.isGameOver()){
            if (MadBalls.isHost()){
                int survivingTeamNum = -1;
                for (Player player : players){
                    if (!player.getBall().isDead()){
                        if (survivingTeamNum < 0){
                            survivingTeamNum = player.getTeamNum();
                        }
                        else if (survivingTeamNum != player.getTeamNum()){
                            return;
                        }
                    }
                }
                if (SceneManager.getInstance().getTeamScoreBoard().size() == 1) return;
//                System.out.println("survive" + survivingTeamNum);
                if (survivingTeamNum != -1){
                    MadBalls.getMultiplayerHandler().sendData(new WinnerData(survivingTeamNum));
                    SceneManager.getInstance().addScore(survivingTeamNum, 1);
                }
                MadBalls.getMultiplayerHandler().newMatch(players.size() == 1);
            }
            return;
        }
//        for (Player player : players){
//            if (player != localPlayer && !player.getBall().isDead()) return;
//        }
////        MadBalls.setGameOver(true);
//        Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
        int teamNum = MadBalls.getMultiplayerHandler().getLocalPlayer().getTeamNum();
        boolean allyAlive = false, enemyAlive = false;
        for (Player player: players){
            if (!player.getBall().isDead()){
                if (player.getTeamNum() == teamNum) allyAlive = true;
                if (player.getTeamNum() != teamNum) enemyAlive = true;
            }
        }

        if (!allyAlive){
            MadBalls.setGameOver(true);
            Navigation.getInstance().showAlert("Game over", "You lose!", "Better luck next time.", false);
        }
        else if (!enemyAlive) {
            MadBalls.setGameOver(true);
            Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
        }
    }
}
