package madballs.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.StateLoader;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.player.Player;

/**
 * Client class handles major socket operations on the client side
 * @author Caval
 */
public class Client extends MultiplayerHandler{
    
    /**
     * connect to the server as client via socket
     */
    public void init(){
        // get the server ip and port
        setService(new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            // connect socket
//                            System.out.println("why");
                            Socket socket = new Socket("127.0.0.1", 8099);
                            setLocalPlayer(new Player(socket, false));
                                    
                                // maintain the socket connection
                                while(true){
                                    try {
                                        System.out.println("handle");
                                        handleData(getLocalPlayer().readData());
//                                        getLocalPlayer().sendData(new Data("haha"));
                                    }
                                    finally {
                                        
                                    }
                                }
                        }
                        catch (IOException ex){
                            // retry if could not connect to server
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        });
        getService().start();
    }

    @Override
    public void sendData(Data data) {
        getLocalPlayer().sendData(data);
    }
    
    @Override
    public void handleData(Data data){
        super.handleData(data);
        if (data.getType().equals("start")){
            MadBalls.getGameEnvironment().startAnimation();
        }
        else if (data.getType().equals("state")){
            try {
                StateData stateData = (StateData)data;
                int objectIndex = stateData.getState().getObjectIndex();
                StateLoader stateLoader = MadBalls.getGameEnvironment().getObject(objectIndex).getStateLoader();
                stateLoader.addServerState(stateData.getState());
            } catch (Exception ex){
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if (data.getType().equals("spawn")){
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
        else if (data.getType().equals("move")){
            moveObject((MoveData) data);
        }
    }
    
    private void spawn(SpawnData data){
        try {
            if (data.getSpawntype().equals("player")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (getLocalPlayer().getBall() == null){
                            getLocalPlayer().setTeamNum(data.getTypeNum());
                            getLocalPlayer().setSpawnLocation(new SpawnLocation(data.getX(), data.getY(), data.getSpawntype(), data.getTypeNum()));
                            getPlayers().add(getLocalPlayer());
                            getLocalPlayer().generateBall(MadBalls.getGameEnvironment());  
                        }
                        else {
                            Player newPlayer = new Player(null, false);
                            newPlayer.setTeamNum(data.getTypeNum());
                            newPlayer.setSpawnLocation(new SpawnLocation(data.getX(), data.getY(), data.getSpawntype(), data.getTypeNum()));
                            getPlayers().add(newPlayer);
                            newPlayer.generateBall(MadBalls.getGameEnvironment());
                            sendData(new Data("ready"));
                        }
                    }
                });
                
            }
            else if (data.getSpawntype().equals("weapon")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        MadBalls.getGameEnvironment().getItemSpawner().spawnWeapon((int)data.getX(), (int)data.getY(), data.getTypeNum());
                    }
                });
            }
            else if (data.getSpawntype().equals("item")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        MadBalls.getGameEnvironment().getItemSpawner().spawnItem((int)data.getX(), (int)data.getY());
                    }
                });
            }
        }
        catch (Exception ex){
            Logger.getLogger(MultiplayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void moveObject(MoveData data){
        try {
            GameObject obj = MadBalls.getGameEnvironment().getObject(data.getObjIndex());
            obj.getMoveBehaviour().setLastMoveTime(data.getLastMoveTime());
            
            if (data.getMoveType() == 1){
                obj.setOldX(data.getOldX());
                obj.setOldY(data.getOldY());
                obj.getMoveBehaviour().setNewX(data.getNewX());
                obj.getMoveBehaviour().setNewY(data.getNewY());
            }
            else if (data.getMoveType() == 2){
                obj.setRotate(data.getNewRotate());
                obj.setOldDirection(data.getOldRotate());
            }
        }
        catch (Exception ex){
            Logger.getLogger(MultiplayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
