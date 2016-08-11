/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.util.ArrayList;
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
        players.add(player);
    }
    
    public void handleData(Data data){
//        System.out.println(data.getType());
//        System.out.println(MadBalls.getGameEnvironment().getNumObjects());
    }
    
    public abstract void sendData(Data data);
    public abstract void init();
}
