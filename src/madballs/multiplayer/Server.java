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
import madballs.AI.BotPlayer;
import madballs.MadBalls;
import madballs.player.Player;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;
import madballs.scenes.ScenesFactory;
import madballs.scenes.controller.GameRoomController;
import madballs.wearables.Pistol;

/**
 *
 * @author Caval
 */
public class Server extends MultiplayerHandler{
    private int playerIndex = 0;
    private int numPlayers = 2;

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Server() {
        super(true);
    }
    
    public void init(){
        numPlayers = MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocations().size();
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

//                                getLocalPlayer().setSpawnLocation(MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocation(1));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        setLocalPlayer(new Player(null, true));
                                        getPlayers().add(getLocalPlayer());
                                        getLocalPlayer().setPlayerNum(++playerIndex);
                                        ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).displayPlayer(getLocalPlayer());
//                                        getLocalPlayer().generateBall(MadBalls.getMainEnvironment(), -1);
                                        getLocalPlayer().setReady(true);
//                                        MadBalls.getMainEnvironment().startAnimation();

                                    }
                                });
                                // listen for client over socket
                                while(true){
                                    try {
                                        if (getPlayers().size() == numPlayers){
                                            return null;
                                        }
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
                                Data data = player.readData();
                                handleData(player, data);
                            }
                            catch (NullPointerException ex) {
                                ex.printStackTrace();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        player.setTeamNum(-1);
                                        sendData(new PlayerData(player));
                                        getPlayers().remove(player);
                                        SceneManager.getInstance().reloadScoreBoard();
                                        ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).updatePlayersPane();
                                    }
                                });
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
        handleData(newPlayer, newPlayer.readData());
        newPlayer.setPlayerNum(++playerIndex);
//        newPlayer.setSpawnLocation(MadBalls.getMainEnvironment().getMap().getPlayerSpawnLocation(playerIndex));
        sendInfoToNewPlayer(newPlayer);
        announceNewPlayer(newPlayer);
        getPlayers().add(newPlayer);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).displayPlayer(newPlayer);
//                newPlayer.generateBall(MadBalls.getMainEnvironment(), -1);
            }
        });
        
        waitAndHandleData(newPlayer);
    }

    public BotPlayer addBotPlayer(){
        BotPlayer bot = new BotPlayer();
        bot.setPlayerNum(++playerIndex);
        sendData(new PlayerData(bot, false));
        getPlayers().add(bot);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((GameRoomController) ScenesFactory.getInstance().getFxmlLoader().getController()).displayPlayer(bot);
//                newPlayer.generateBall(MadBalls.getMainEnvironment(), -1);
            }
        });
        return bot;
    }
    
    public void sendInfoToNewPlayer(Player newPlayer){
        newPlayer.sendData(new MapData(MadBalls.getMainEnvironment().getMap().getMapNumber()));
        System.out.println(4);
        for (Player player : getPlayers()){
            System.out.println("1");
            newPlayer.sendData(new PlayerData(player, false));
//            newPlayer.sendData(new SpawnData(player.getSpawnLocation(), false, player.getBall().getID()));
        }
    }
    
    public void announceNewPlayer(Player newPlayer){
        try {
//            Integer newBallID = MadBalls.getMainEnvironment().getCurrentObjID();
            sendData(new PlayerData(newPlayer, false));
            newPlayer.sendData(new PlayerData(newPlayer, true));
//            sendData(new SpawnData(newPlayer.getSpawnLocation(), false, newBallID));
//            newPlayer.sendData(new SpawnData(newPlayer.getSpawnLocation(), true, newBallID));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void sendData(Data data) {
        for (Player player : getPlayers()){
            if (player != getLocalPlayer()){
                if (player instanceof BotPlayer){
                    BotPlayer bot = (BotPlayer) player;
                    bot.getBotClient().handleData(data);
                }
                else {
                    player.sendData(data);
                }
            }
        }
    }
    
    public void handleData(Player currentPlayer, Data data){
//        System.out.println("handle" + data.getType());
        super.handleData(data);
        if (data.getType().equals("ready")){
            ReadyData readyData = (ReadyData) data;
            currentPlayer.setReady(true);
            currentPlayer.getController().setSceneWidth(readyData.getSceneWidth());
            currentPlayer.getController().setSceneHeight(readyData.getSceneHeight());
            System.out.println(readyData.getSceneWidth());
            System.out.println(readyData.getSceneHeight());
            System.out.println("index" +playerIndex);
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
                    for (BotPlayer bot: BotPlayer.getBotPlayers()){
                        bot.play();
                    }
                }
            });
        }
        else if (data.getType().equals("input_key")){
            KeyInputData keyInputData = (KeyInputData) data;
            currentPlayer.getKeyHandler().handle(keyInputData.getKeyEvent());
        }
        else if (data.getType().equals("input_mouse")){
            MouseInputData mouseInputData = (MouseInputData) data;
            currentPlayer.getMouseHandler().handle(mouseInputData);
        }
        else if (data.getType().equals("player")){
            PlayerData playerData = (PlayerData) data;
            System.out.println("new player" + playerData.getName());
            currentPlayer.setName(playerData.getName());
        }
    }
}
