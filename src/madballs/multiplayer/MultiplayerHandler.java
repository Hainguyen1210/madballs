/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.concurrent.Service;
import madballs.Environment;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.player.Player;

/**
 *
 * @author caval
 */
public abstract class MultiplayerHandler {
    private Service<Void> service;
    private Player localPlayer;
    private ArrayList<Player> players = new ArrayList<>();
    
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
        System.out.println(data.getType());
        if (data.getType().equals("spawn")){
            spawn((SpawnData)data);
        }
        else if (data.getType().equals("choose_map")){
            System.out.println("map");
            Map map = new Map(MadBalls.RESOLUTION_X, MadBalls.RESOLUTION_Y, ((MapData)data).getMapNumber());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MadBalls.getGameEnvironment().loadMap(map);
                }
            });
        }
    }
    
    private void spawn(SpawnData data){
        if (data.getSpawntype().equals("player")){
            Player newPlayer = new Player(null, false);
            newPlayer.setTeamNum(data.getTypeNum());
            newPlayer.setSpawnLocation(new SpawnLocation(data.getX(), data.getY(), data.getSpawntype(), data.getTypeNum()));
            players.add(newPlayer);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    newPlayer.generateBall(MadBalls.getGameEnvironment());
                }
            });
        }
    }
    
    public abstract void init();
}
