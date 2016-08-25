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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import madballs.*;
import madballs.map.Map;
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
    private boolean isLocal;
    private boolean isReady;
    private int playerNum;
    private int teamNum;
    private SpawnLocation spawnLocation;
    private ArrayList<Integer> relevantObjIDs = new ArrayList<>();

    public ArrayList<Integer> getRelevantObjIDs() {
        return relevantObjIDs;
    }

    public void setRelevantObjIDs(ArrayList<Integer> relevantObjIDs) {
        this.relevantObjIDs = relevantObjIDs;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public Socket getSocket() {
        return socket;
    }

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
    
    public boolean isLocal(){
        return isLocal;
    }

    public void checkRelevancy(GameObject obj){
        double xDiff = Math.abs(obj.getTranslateX() - ball.getTranslateX());
        double yDiff = Math.abs(obj.getTranslateY() - ball.getTranslateY());
        SubScene animationScene = MadBalls.getAnimationScene();
        double scale = SceneManager.getInstance().getScale();
        if (xDiff < animationScene.getWidth()/2/scale + 100 && yDiff < animationScene.getHeight()/2/scale + 100){
            relevantObjIDs.add(obj.getID());
        }
//        relevantObjIDs.add(obj.getID());

    }

    public Player(Socket socket, boolean isLocal){
        controller = new Controller(this);
        this.isLocal = isLocal;
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
        double ballSize = ball.getHitBox().getLayoutBounds().getHeight();
        ball.setImage(ImageGenerator.getInstance().getImage("ball"+2));
        ball.getImage().setFitHeight(ballSize);
        ball.getImage().setFitWidth(ballSize);
        ball.getImage().setTranslateX(-ballSize/2);
        ball.getImage().setTranslateY(-ballSize/2);

        if (ImageGenerator.getInstance().getImage("ball"+teamNum) == null) System.out.print("11111111111111111111111111111111111111111111111111111111111111111111111");
        if (isLocal) {
            bindInput(MadBalls.getMainScene());
            SceneManager.getInstance().bindBall(ball);
        }
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
    
    private final MultiplePressedKeysEventHandler keyHandler = 
        new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {
            
            @Override
            public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
//                if (!MadBalls.isHost()) return;
                controller.handleKey(ke);
            }
        }, this);
    
    private final MouseKeyEventHandler mouseHandler = 
        new MouseKeyEventHandler(new MouseKeyEventHandler.MouseEventHandler() {
            @Override
            public void handle(MouseKeyEventHandler.MouseKeyEvent event) {
//                if (!MadBalls.isHost()) return;
                controller.handleMouse(event);
            }
        }, this);

    public MultiplePressedKeysEventHandler getKeyHandler() {
        return keyHandler;
    }

    public MouseKeyEventHandler getMouseHandler() {
        return mouseHandler;
    }
    
    public void bindInput(Scene scene){
//
//        camera.setTranslateX(ball.getTranslateX());
//        camera.setTranslateY(ball.getTranslateY());
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
        try {
            out.writeObject(data);
            out.flush();
//            System.out.println("sent " + data.getType());
//            System.out.println(Environment.getInstance().gameNumObjects());
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Data readData(){
        try {
            return (Data) in.readObject();
        } catch (ClassNotFoundException | IOException ex){
            Platform.exit();
        } catch (NullPointerException ex){
            MadBalls.getMultiplayerHandler().getPlayers().remove(this);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ball.die();
                }
            });
        }
        return null;
    }
}
