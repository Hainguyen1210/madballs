/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import madballs.Ball;
import madballs.Environment;
import madballs.map.SpawnLocation;
import madballs.multiplayer.Data;

/**
 *
 * @author Caval
 */
public class Player {
    private Ball ball;
    private Controller controller;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final BooleanProperty isHost = new SimpleBooleanProperty(false);
    private int playerNum;
    private int teamNum;
    private SpawnLocation spawnLocation;

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(SpawnLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
    
    public Controller getController(){
        return controller;
    }
    
    public ObjectOutputStream out(){
        return out;
    }
    
    public ObjectInputStream in(){
        return in;
    }
    
    public boolean isHost(){
        return isHost.get();
    }
    
    public Player(Socket socket, boolean isHost){
        controller = new Controller(this);
        this.isHost.set(isHost);
        this.socket = socket;
        if (socket != null){
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void generateBall(Environment environment){
        ball = new Ball(environment, spawnLocation.getX(), spawnLocation.getY());
    }
    
    public Ball getBall(){
        return ball;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getTeamNum() {
        return teamNum;
    }
    
    public void setTeamNum(int teamNum){
        this.teamNum = teamNum;
    }
    
    final MultiplePressedKeysEventHandler keyHandler = 
        new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {
            
            public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                if (isHost()){
                    controller.handleKey(ke);
                }
            }
        });
    
    final MouseKeyEventHandler mouseHandler = new MouseKeyEventHandler(new MouseKeyEventHandler.MouseEventHandler() {
        @Override
        public void handle(MouseKeyEventHandler.MouseKeyEvent event) {
            if (isHost()){
                controller.handleMouse(event);
            }
        }
    });
    
    public void bindInput(Scene scene){
//        System.out.println("123");
//        scene.setOnKeyPressed(ball.getMoveBehaviour().keyHandler);
//        scene.setOnKeyReleased(ball.getMoveBehaviour().keyHandler);
//        scene.setOnMousePressed(ball.getWeapon().getMoveBehaviour().mouseHandler);
//        scene.setOnMouseReleased(ball.getWeapon().getMoveBehaviour().mouseHandler);
//        scene.setOnMouseMoved(ball.getWeapon().getMoveBehaviour().mouseHandler);
//        scene.setOnMouseDragged(ball.getWeapon().getMoveBehaviour().mouseHandler);
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
    }
    
    public void sendData(Data data){
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            out.writeObject(data);
                            System.out.println("sent");
                        } catch (IOException ex) {
                            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        };
        service.start();
    }
    
    public Data readData(){
        try {
            System.out.println("2");
            Data data = null;
            in.readObject();
            System.out.println("3");
            return data;
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
