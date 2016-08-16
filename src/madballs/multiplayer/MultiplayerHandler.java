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
        this.localPlayer = player;
//        player.getBall().getEnvironment().
        players.add(player);
    }
    
    public void handleData(Data data){
//        System.out.println(data.getType());
//        System.out.println(Environment.getInstance().getNumObjects());
    }
    
    public void checkWinner(){
        if (MadBalls.isGameOver()) return;
        if (localPlayer.getBall().isDead()){
            MadBalls.setGameOver(true);
            MadBalls.getNavigation().showAlert("Game over", "You lose!", "Better luck next time.", false);
            return;
        }
        for (Player player : players){
            if (player != localPlayer && !player.getBall().isDead()) return;
        }
        MadBalls.setGameOver(true);
        MadBalls.getNavigation().showInterupt("Victory", "You won!", "It was a glorious victory!", false);
    }
    
    public abstract void sendData(Data data);
    public abstract void init();
}
