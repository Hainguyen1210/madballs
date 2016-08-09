/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import madballs.MadBalls;
import madballs.player.Player;

/**
 *
 * @author Caval
 */
public class Server extends MultiplayerHandler{
    private int playerIndex = 0;
    
    public void init(){
        setService(new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        ServerSocket listener;
                        try {
                            listener = new ServerSocket(8099);
                            try {       
                                setLocalPlayer(new Player(null, true));
                                getLocalPlayer().setPlayerNum(playerIndex++);
                                getLocalPlayer().setSpawnLocation(MadBalls.getGameEnvironment().getMap().getPlayerSpawnLocation(1));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getLocalPlayer().generateBall(MadBalls.getGameEnvironment());
                                        getLocalPlayer().bindInput(MadBalls.getScene());
                                    }
                                });                                
                                // listen for client over socket
                                while(true){
                                    try {
                                        Socket socket = listener.accept();
                                        addNewPlayer(socket);
                                    }
                                    finally {
                                        
                                    }
                                }
                            } finally {
                                listener.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        });
        getService().start();
    }
    
    public void waitAndHandleData(Player player){
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            System.out.println("ok");
                            handleData(player.readData());
                            player.sendData(new Data("hihi"));
                        }
                    }
                };
            }
        };
        service.start();
    }
    
    public void addNewPlayer(Socket socket){
        Player newPlayer = new Player(socket, false);
        newPlayer.sendData(new Data("hehe"));
        sendInfoToNewPlayer(newPlayer);
        newPlayer.setPlayerNum(playerIndex++);
        newPlayer.setSpawnLocation(MadBalls.getGameEnvironment().getMap().getPlayerSpawnLocation(2));
        anounceNewPlayer(newPlayer);
        getPlayers().add(newPlayer);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                newPlayer.generateBall(MadBalls.getGameEnvironment());
            }
        });
        
        waitAndHandleData(newPlayer);
    }
    
    public void sendInfoToNewPlayer(Player newPlayer){
        newPlayer.sendData(new MapData(MadBalls.getGameEnvironment().getMap().getMapNumber()));
        for (Player player : getPlayers()){
            System.out.println("1");
            newPlayer.sendData(new SpawnData(player.getSpawnLocation()));
        }
    }
    
    public void anounceNewPlayer(Player newPlayer){
        for (Player player : getPlayers()){
            if (player != getLocalPlayer()){
                player.sendData(new SpawnData(newPlayer.getSpawnLocation()));
            }
        }
    }
}
