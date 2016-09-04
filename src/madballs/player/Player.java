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
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import madballs.*;
import madballs.AI.BotPlayer;
import madballs.map.SpawnLocation;
import madballs.multiplayer.Data;
import madballs.multiplayer.PlayerData;
import madballs.scenes.SceneManager;

/**
 *
 * @author Caval
 */
public class Player {
    private String name;
    private Ball ball;
    private Controller controller;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isLocal;
    private boolean isReady;
    private int playerNum;
    private IntegerProperty teamNum = new SimpleIntegerProperty();
    private SpawnLocation spawnLocation;
    private ArrayList<Integer> relevantObjIDs = new ArrayList<>();

    private IntegerProperty killsCount = new SimpleIntegerProperty(0);
    private IntegerProperty deathsCount = new SimpleIntegerProperty(0);
    private IntegerProperty ranking = new SimpleIntegerProperty(0);

    public int getRanking() {
        return ranking.get();
    }

    public IntegerProperty rankingProperty() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking.set(ranking);
    }

    public int getKillsCount() {
        return killsCount.get();
    }

    public IntegerProperty killsCountProperty() {
        return killsCount;
    }

    public void setKillsCount(int killsCount) {
        this.killsCount.set(killsCount);
    }

    public int getDeathsCount() {
        return deathsCount.get();
    }

    public IntegerProperty deathsCountProperty() {
        return deathsCount;
    }

    public void setDeathsCount(int deathsCount) {
        this.deathsCount.set(deathsCount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void checkRelevancy(GameObject obj, double varianceX, double varianceY){
        if (obj.isDead()) {
            if (this instanceof BotPlayer){
                relevantObjIDs.remove(obj);
            }
            else {
                if (!relevantObjIDs.contains(obj.getID())) {
                    relevantObjIDs.add(obj.getID());
                }
            }
            return;
        }
        if (getRelevancy(obj.getTranslateX(), obj.getTranslateY(), varianceX, varianceY)) {
            if (!relevantObjIDs.contains(obj.getID())) {
                relevantObjIDs.add(obj.getID());
            }
        }
        else {
            relevantObjIDs.remove(obj.getID());
        }

    }

    public boolean getRelevancy(double x, double y, double varianceX, double varianceY){
        double xDiff = Math.abs(x - ball.getTranslateX());
        double yDiff = Math.abs(y - ball.getTranslateY());
//        double numMapParts = SceneManager.NUM_MAP_PARTS;
//        double zoomOut = getBall().getWeapon().getScope();
//        double scale = 1 / numMapParts * zoomOut / 2;
        double scale = controller.getScale();
        return  (xDiff < controller.getSceneWidth()/2/scale + varianceX && yDiff < controller.getSceneHeight()/2/scale + varianceY);
//        return  (xDiff < controller.getSceneWidth()*scale + varianceX && yDiff < controller.getSceneHeight()*scale + varianceY);
    }

    public Player(Socket socket, boolean isLocal){
        controller = new Controller(this);
        this.isLocal = isLocal;
        this.socket = socket;
        setSocket(socket);

        ranking.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SceneManager.getInstance().reorderScoreBoard();
            }
        });
    }

    public void updateRanking(){
        for (Player player : MadBalls.getMultiplayerHandler().getPlayers()){
            if (player == this) continue;
            if (getKillsCount() > player.getKillsCount() || (getKillsCount() == player.getKillsCount() && getDeathsCount() < player.getDeathsCount())){
                if (getRanking() >= player.getRanking()){
                    int newRanking = player.getRanking();
                    player.setRanking(getRanking());
                    setRanking(newRanking);
//                    System.out.println("update rank" + name + getRanking());
                    player.updateRanking();
                }
            }
        }
    }

    public void setSocket(Socket socket){
        if (socket != null){
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void generateBall(Environment environment, Integer id){
        ball = new Ball(environment, spawnLocation.getX(), spawnLocation.getY(), id, this);
        SceneManager.getInstance().bindBallToScoreBoard(ball);

        Label nameLabel = new Label(name);
        nameLabel.setTranslateY(2);
        nameLabel.setMaxWidth(40);
        nameLabel.setFont(new Font(10));
        nameLabel.setTextFill(Color.WHITE);

        ImageView badge = new ImageView(ImageGenerator.getInstance().getImage("blue_badge"));
        badge.setFitWidth(70);
        badge.setFitHeight(18);
        Pane namePane = new Pane(badge, nameLabel);
        namePane.setTranslateY(-60);
        namePane.setTranslateX(-15);
        ball.getStatusG().getChildren().add(namePane);
        nameLabel.translateXProperty().bind(Bindings.divide(Bindings.subtract(55, nameLabel.widthProperty()), 2));

        double ballSize = ball.getHitBox().getLayoutBounds().getHeight();
        ball.setImage("ball"+teamNum.get());
        ball.getImageView().setFitHeight(ballSize);
        ball.getImageView().setFitWidth(ballSize);
        ball.getImageView().setTranslateX(-ballSize/2);
        ball.getImageView().setTranslateY(-ballSize/2);

        if (isLocal) {
            bindInput();
            SceneManager.getInstance().bindBall(ball);
            controller.setSceneWidth(MadBalls.getAnimationScene().getWidth());
            controller.setSceneHeight(MadBalls.getAnimationScene().getHeight());
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

    public IntegerProperty teamNumProperty() {
        return teamNum;
    }

    public int getTeamNum() {
        return teamNum.get();
    }
    
    public void setTeamNum(int teamNum){
        this.teamNum.set(teamNum);
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
    
    public void bindInput(){
        Scene scene = MadBalls.getMainScene();
        scene.setOnKeyPressed(keyHandler);
        scene.setOnKeyReleased(keyHandler);

        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
    }

    public void sendData(Data data){
        if (out == null) return;
        MadBalls.getMultiplayerHandler().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(data);
//                    out.flush();
//            System.out.println("sent " + data.getType());
//            System.out.println(Environment.getInstance().gameNumObjects());
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public Data readData(){
        try {
            return (Data) in.readObject();
        } catch (ClassNotFoundException | IOException | NullPointerException ex){
            MadBalls.getMultiplayerHandler().getPlayers().remove(this);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (ball != null) ball.die();
                }
            });
        }
        return null;
    }
}
