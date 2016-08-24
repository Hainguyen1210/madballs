/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.concurrent.Service;
import madballs.MadBalls;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.ScenesFactory;
import madballs.scenes.controller.GameRoomController;

/**
 *
 * @author caval
 */
public abstract class MultiplayerHandler {
    private Service<Void> service;
    private Player localPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean isHost;
    protected long latency;
    
    public MultiplayerHandler(boolean isHost){
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }
    
    public ArrayList<Player> getPlayers(){
        return players;
    }
    
    public Player getLocalPlayer(){
        return localPlayer;
    }
    
    public Service<Void> getService() {
        return service;
    }

    public void setService(Service<Void> service) {
        this.service = service;
    }
    
    public void setLocalPlayer(Player player){
        player.setName(Navigation.getInstance().getTextResponse("Create player", "Welcome!", "Please enter your name", "Ball"));
        this.localPlayer = player;
//        player.getBall().getEnvironment().
//        System.out.println(((GameRoomController)ScenesFactory.getInstance().getFxmlLoader().getController()) == null);
    }
    
    public void handleData(Data data){
        if (data.getType().equals("update_player")){
            System.out.println("update");
            PlayerData playerData = (PlayerData) data;
            for (Player player : players){
                if (player.getPlayerNum() == playerData.getNumber()){
                    System.out.println("update correct" + playerData.getTeamNumber());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (player.getTeamNum() >= 0) {
                                player.setTeamNum(playerData.getTeamNumber());
                            }
                            else {
                                getPlayers().remove(player);
                                ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).updatePlayersPane();
                            }
                            ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).updateTeamChoices();
                        }
                    });
                }
            }
        }
//        System.out.println(data.getType());
//        System.out.println(Environment.getInstance().getNumObjects());
    }
    
    public void checkWinner(){
        if (MadBalls.isGameOver()){
            if (MadBalls.isHost()){
                if (((Server)this).getPlayerIndex() == 1) return;
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
                prepareNewGame();
            }
            return;
        }
//        if (localPlayer.getBall().isDead()){
////            MadBalls.setGameOver(true);
//            Navigation.getInstance().showAlert("Game over", "You lose!", "Better luck next time.", false);
//            return;
//        }
//        for (Player player : players){
//            if (player != localPlayer && !player.getBall().isDead()) return;
//        }
////        MadBalls.setGameOver(true);
//        Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
        int teamNum = localPlayer.getTeamNum();
        boolean allyAlive = false, enemyAlive = false;
        for (Player player: players){
            if (!player.getBall().isDead()){
                if (player.getTeamNum() == teamNum) allyAlive = true;
                if (player.getTeamNum() != teamNum) enemyAlive = true;
            }
        }

        if (!allyAlive){
            Navigation.getInstance().showAlert("Game over", "You lose!", "Better luck next time.", false);
            MadBalls.setGameOver(true);
        }
        else if (!enemyAlive) {
            Navigation.getInstance().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
            MadBalls.setGameOver(true);
        }
    }

    public void prepareNewGame(){
        MadBalls.getMainEnvironment().stopAnimation();
        MadBalls.newGame(true);
        Navigation.getInstance().navigate(ScenesFactory.getInstance().newScene("prepare"));
        sendData(new Data("restart"));
    }
    
    public abstract void sendData(Data data);
    public abstract void init();
}
