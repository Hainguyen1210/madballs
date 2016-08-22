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
import madballs.Environment;
import madballs.MadBalls;
import madballs.player.Player;

/**
 *
 * @author Caval
 */
public class Server extends MultiplayerHandler{
    private int playerIndex = 0;

    public Server() {
        super(true);
    }
    
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
                                getLocalPlayer().setPlayerNum(++playerIndex);
                                getLocalPlayer().setSpawnLocation(MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocation(1));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getLocalPlayer().generateBall(MadBalls.getMainEnvironment());
                                        getLocalPlayer().setReady(true);
                                        MadBalls.getMainEnvironment().startAnimation();

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
                        while (true){
                            try {
                                handleData(player, player.readData());
                            }
                            catch (Exception ex) {
//                                System.out.println("12");
                                Logger.getLogger(MultiplayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                                return null;
                            }
                        }
                    }
                };
            }
        };
        service.start();
    }
    
    public void addNewPlayer(Socket socket){
        Player newPlayer = new Player(socket, false);
        newPlayer.setPlayerNum(++playerIndex);
        newPlayer.setSpawnLocation(MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocation(playerIndex));
        sendInfoToNewPlayer(newPlayer);
        anounceNewPlayer(newPlayer);
        getPlayers().add(newPlayer);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                newPlayer.generateBall(MadBalls.getMainEnvironment());
            }
        });
        
        waitAndHandleData(newPlayer);
    }
    
    public void sendInfoToNewPlayer(Player newPlayer){
        newPlayer.sendData(new MapData(MadBalls.getMainEnvironment().getMap().getMapNumber()));
        for (Player player : getPlayers()){
            System.out.println("1");
            newPlayer.sendData(new SpawnData(player.getSpawnLocation(), false));
        }
    }
    
    public void anounceNewPlayer(Player newPlayer){
        sendData(new SpawnData(newPlayer.getSpawnLocation(), false));
        newPlayer.sendData(new SpawnData(newPlayer.getSpawnLocation(), true));
    }

    @Override
    public void sendData(Data data) {
        for (Player player : getPlayers()){
            if (player != getLocalPlayer()){
                if (data instanceof StateData){
                    if (((StateData)data).getState().isDead()){
                        System.out.println("dead" + ((StateData)data).getState().getObjID());
                    }
                    if (!player.getRelevantObjIDs().contains(((StateData)data).getState().getObjID())){
//                        continue;
                    }
                }
                player.sendData(data);
            }
        }
    }
    
    public void handleData(Player currentPlayer, Data data){
        super.handleData(data);
        if (data.getType().equals("ready")){
            currentPlayer.setReady(true);
            System.out.println("index" +playerIndex);
            if (playerIndex >= 2){
                for (Player player : getPlayers()){
                    System.out.println(player.getPlayerNum());
                    System.out.println(player.isReady());
                    if (!player.isReady()) return;
                }
                System.out.println("start");
                sendData(new Data("start"));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MadBalls.getMainEnvironment().startAnimation();
                    }
                });
            }
        }
        else if (data.getType().equals("input_key")){
            KeyInputData keyInputData = (KeyInputData) data;
            currentPlayer.getKeyHandler().handle(keyInputData.getKeyEvent());
        }
        else if (data.getType().equals("input_mouse")){
            MouseInputData mouseInputData = (MouseInputData) data;
            currentPlayer.getMouseHandler().handle(mouseInputData);
        }
    }
}
