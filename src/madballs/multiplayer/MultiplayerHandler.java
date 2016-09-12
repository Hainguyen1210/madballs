/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Service;
import madballs.AI.BotPlayer;
import madballs.MadBalls;
import madballs.map.SpawnLocation;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;
import madballs.scenes.ScenesFactory;
import madballs.scenes.controller.GameRoomController;

/**
 * the class that handles the communication between all players of the game
 * @author caval
 */
public abstract class MultiplayerHandler {
    private Service<Void> service;
    private Player localPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean isHost;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    protected long latency;

    public ExecutorService getExecutorService() {
        return executorService;
    }

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
        player.setName(Navigation.getInstance().getTextResponse("Create player", "Welcome!", "Please enter your name", "MadBall"));
        this.localPlayer = player;
//        player.getBall().getEnvironment().
//        System.out.println(((GameRoomController)ScenesFactory.getInstance().getFxmlLoader().getController()) == null);
    }
    
    public void handleData(Data data){
        if (data.getType().equals("update_player")){
//            System.out.println("update");
            PlayerData playerData = (PlayerData) data;
            for (Player player : players){
                if (player.getPlayerNum() == playerData.getNumber()){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (playerData.getTeamNumber() >= 0) {
                                player.setTeamNum(playerData.getTeamNumber());
                                player.setKillsCount(playerData.getKillsCount());
                                player.setDeathsCount(playerData.getDeathsCount());
                                player.setRanking(playerData.getRanking());
                            }
                            else {
                                getPlayers().remove(player);
                                SceneManager.getInstance().reloadScoreBoard();
                                ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).updatePlayersPane();
                            }
                            ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).updateTeamChoices();
                        }
                    });
                }
            }
            if (isHost) {
                sendData(playerData);
            }
        }
//        System.out.println(data.getType());
//        System.out.println(Environment.getInstance().getNumObjects());
    }

    public void newMatch(boolean isNewGame){
        for (Player player : players){
            if (player instanceof BotPlayer){
                ((BotPlayer) player).stop();
            }
            player.setReady(isHost && player == getLocalPlayer());
            player.getKeyHandler().clear();
            player.getMouseHandler().clear();
            if (isNewGame){
                player.setKillsCount(0);
                player.setDeathsCount(0);
            }
            if (isHost) sendData(new Data(isNewGame? "new_game" : "new_match"));
        }

        MadBalls.getMainEnvironment().stopAnimation();
//        MadBalls.restart();
        if (isNewGame) {
            SceneManager.getInstance().resetTeamScoreBoard();
            Navigation.getInstance().navigate(ScenesFactory.getInstance().newScene("prepare"));
        }
        else if (isHost) {
            startMatch();
        }
    }



    public void startMatch(){
        System.out.println("stMat");
        MadBalls.restart();
        Navigation.getInstance().navigate(MadBalls.getMainScene());

        MadBalls.getMultiplayerHandler().sendData(new Data("prepare"));
        for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
            player.setSpawnLocation(MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocation(player.getTeamNum()));
            player.generateBall(MadBalls.getMainEnvironment(), -1);
            for (Player receivingPlayer : MadBalls.getMultiplayerHandler().getPlayers()){
                if (receivingPlayer != MadBalls.getMultiplayerHandler().getLocalPlayer()){
                    SpawnLocation spawnLocation = player.getSpawnLocation();
                    spawnLocation.setTypeNumber(player.getPlayerNum());
                    receivingPlayer.sendData(new SpawnData(spawnLocation, player == receivingPlayer, player.getBall().getID()));
                }
            }
        }
        if (MadBalls.getMultiplayerHandler().getPlayers().size() == 1){
            MadBalls.getMainEnvironment().startAnimation();
        }
        else {
            MadBalls.getMultiplayerHandler().sendData(new Data("check_ready"));
        }
    }
    
    public abstract void sendData(Data data);
    public abstract void init();
}
