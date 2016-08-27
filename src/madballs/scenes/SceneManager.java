/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.buffState.BuffState;
import madballs.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author caval
 */
public class SceneManager {
    public static final int numMapParts = 3;
    public static final Color[] teamColors = new Color[]{Color.BLUE, Color.RED, Color.PINK, Color.GREEN, Color.BROWN, Color.ORANGE};
    private static SceneManager instance = new SceneManager();
    private FlowPane gameInfoDisplay;
    private ScrollPane scoreBoardContainer;
    private GridPane scoreBoardGrid;
    private Map<Player, HBox> scoreBoard;
    private Map<Integer, Integer> teamScoreBoard = new WeakHashMap<>();
    private HBox teamScoresDisplay;
    private ProgressBar hpBar = new ProgressBar();
    private Label weaponLabel = new Label();
    private HBox buffBar = new HBox(10);
    private Map<String, Label> buffLabels;
    private Rectangle2D primaryScreenBounds;
    private double screenWidth, screenHeight;
    // scale: the ratio of the visual/scene size to the actual game element size.
    // i.e. multiplying an element's size by this scale would give the visual size of the element (the size on scene)
    private DoubleProperty scale = new SimpleDoubleProperty(1);
    // zoomOut: the ratio of how much the game elements have been zoomed out compared to its initial size
    private DoubleProperty zoomOut = new SimpleDoubleProperty(1);
    private PerspectiveCamera camera;

    public Map<Integer, Integer> getTeamScoreBoard() {
        return teamScoreBoard;
    }

    public ScrollPane getScoreBoardContainer() {
        return scoreBoardContainer;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public Rectangle2D getPrimaryScreenBounds() {
        return primaryScreenBounds;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public Pane getGameInfoDisplay() {
        return gameInfoDisplay;
    }

    public double getScale() {
        return scale.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public double getZoomOut() {
        return zoomOut.get();
    }

    public void setZoomOut(double zoomOut) {
        this.zoomOut.set(zoomOut);
    }

    private SceneManager(){
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = primaryScreenBounds.getWidth();
        screenHeight = primaryScreenBounds.getHeight();
//        scaleX = screenWidth / RESOLUTION_X;
//        scaleY = screenHeight / RESOLUTION_Y;
    }
    
    public static SceneManager getInstance(){
        return instance;
    }
    
    public void displayGameInfo(Group root){
        buffLabels = new HashMap<>();
        buffBar.getChildren().clear();

        hpBar.setPrefSize(250, 25);
        hpBar.setTranslateX(50);
        weaponLabel.setTranslateX(120);
        buffBar.setTranslateX(140);
        buffBar.setTranslateY(3);

        gameInfoDisplay = new FlowPane(hpBar, weaponLabel, buffBar);
        gameInfoDisplay.setAlignment(Pos.CENTER_LEFT);
        gameInfoDisplay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        gameInfoDisplay.setPrefWidth(MadBalls.getAnimationScene().getWidth());
        gameInfoDisplay.setPrefHeight(30);
        gameInfoDisplay.translateYProperty().bind(Bindings.subtract(MadBalls.getAnimationScene().getHeight(), gameInfoDisplay.heightProperty()));
//        gameInfoDisplay.setTranslateY(MadBalls.getMainScene().getHeight() - 30);
        gameInfoDisplay.setTranslateZ(-1);


        teamScoresDisplay = new HBox(20);
        teamScoresDisplay.setPrefHeight(40);
        scoreBoardGrid = new GridPane();
        AnchorPane anchorPane = new AnchorPane(scoreBoardGrid);
        scoreBoardContainer = new ScrollPane(anchorPane);
        scoreBoardContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        scoreBoardContainer.setPrefSize(400, MadBalls.getAnimationScene().getHeight()*2/3);
        scoreBoardContainer.setTranslateZ(-1);
        scoreBoardContainer.setTranslateX((MadBalls.getAnimationScene().getWidth() - 400)/2);
        scoreBoardContainer.setTranslateY((MadBalls.getAnimationScene().getHeight() - scoreBoardContainer.getPrefHeight())/2);
        scoreBoardContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scoreBoardContainer.setVisible(false);
        reloadScoreBoard();

        root.getChildren().addAll(gameInfoDisplay, scoreBoardContainer);
    }

    public void resetTeamScoreBoard(){
        teamScoreBoard = new WeakHashMap<>();
    }

    public void reloadTeamScores(){
        teamScoresDisplay.getChildren().clear();
        for (Integer teamNum : teamScoreBoard.keySet()){
            Label score = new Label(String.format("%d", teamScoreBoard.get(teamNum)));
            System.out.println("score" + teamScoreBoard.get(teamNum));
            System.out.println(teamNum);
            score.setFont(new Font(20));
            score.setTextFill(teamColors[teamNum-1]);
            teamScoresDisplay.getChildren().add(score);
        }
        teamScoresDisplay.setAlignment(Pos.CENTER);
    }

    public void addScore(int teamNum, int additionalScore){
        int oldScore = teamScoreBoard.get(teamNum);
        System.out.println("oldScore" + oldScore);
        teamScoreBoard.replace(teamNum, oldScore + additionalScore);
        System.out.println("newScore" + teamScoreBoard.get(teamNum));
        reloadTeamScores();
    }

    public void reloadScoreBoard(){
        scoreBoard = new WeakHashMap<>();
        for (Player player : MadBalls.getMultiplayerHandler().getPlayers()){
            if (!teamScoreBoard.containsKey(player.getTeamNum())){
                teamScoreBoard.put(player.getTeamNum(), 0);
            }

            HBox playerHBox = new HBox(15);
            Label playerName = new Label(player.getName());
            Label playerTeam = new Label("team " + Integer.toString(player.getTeamNum()));
            playerTeam.setTextFill(teamColors[player.getTeamNum() - 1]);

            Label killsCount = new Label();
            killsCount.setTextFill(Color.GREEN);
            killsCount.textProperty().bind(Bindings.format("%d", player.killsCountProperty()));

            Label deathsCount = new Label();
            deathsCount.setTextFill(Color.RED);
            deathsCount.textProperty().bind(Bindings.format("%d", player.deathsCountProperty()));

            playerHBox.getChildren().addAll(playerName,playerTeam,killsCount,deathsCount);
            playerHBox.setPrefWidth(400);
            if (player == MadBalls.getMultiplayerHandler().getLocalPlayer()) playerHBox.setStyle("-fx-background-color: rgba(0, 0, 255, 0.12);");
            scoreBoard.put(player, playerHBox);
        }
        reloadTeamScores();
        reorderScoreBoard();
    }

    public void reorderScoreBoard(){
        if (scoreBoardGrid == null) return;
        scoreBoardGrid.getChildren().clear();
        scoreBoardGrid.add(teamScoresDisplay, 0 ,0);
        for (Player player: scoreBoard.keySet()){
            if (player.getRanking() == 0) continue;
            scoreBoardGrid.add(scoreBoard.get(player), 0, player.getRanking());
            System.out.println("reorder"+player.getName() + player.getRanking());
        }
    }

    public void bindBallToScoreBoard(Ball ball){
        Label isDead = new Label("dead");
        isDead.setVisible(false);
        isDead.setTextFill(Color.RED);

        ball.getHp().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((Double)newValue <= 0){
                    isDead.setVisible(true);
                }
                else {
                    isDead.setVisible(false);
                }
            }
        });

        Label hpLabel = new Label();
        hpLabel.textProperty().bind(Bindings.format("%,.1f%s", ball.getHp(), "%"));
        hpLabel.setPrefWidth(50);
        if (ball.getPlayer().getTeamNum() != MadBalls.getMultiplayerHandler().getLocalPlayer().getTeamNum()){
            hpLabel.setOpacity(0);
        }

        scoreBoard.get(ball.getPlayer()).getChildren().addAll(hpLabel, isDead);
    }
    
    public void bindCamera(GameObject obj){
        System.out.println("asd" + MadBalls.getAnimationScene().getHeight());
        scale.bind(Bindings.divide(MadBalls.getAnimationScene().getHeight() / MadBalls.getMainEnvironment().getMap().getHeight() * numMapParts, zoomOut));
//        scale.bind(Bindings.divide(
//                Bindings.divide(MadBalls.getAnimationScene().heightProperty(), MadBalls.getMainEnvironment().getMap().getHeight() / numMapParts)
//                , zoomOut));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        // display the map by how many vertical parts it has been divided into and how much it has been zoom out
        // e.g. if the map is 720px high, divided into 3 parts, and zoomed out by 1.5, the displaying height is 720/3*1.5 = 360
        camera.translateZProperty().bind(Bindings.multiply(zoomOut, -MadBalls.getMainEnvironment().getMap().getHeight() / numMapParts / Math.tan(Math.toRadians(30))));
        camera.setFieldOfView(30);
        camera.translateXProperty().bind(obj.getTranslateXProperty());
        camera.translateYProperty().bind(obj.getTranslateYProperty());
        MadBalls.getMainEnvironment().getDisplay().getChildren().add(camera);
        MadBalls.getAnimationScene().setCamera(camera);
    }

    public void bindWeaponInfo(Ball ball){
        if (ball.getWeapon().getAmmo() >= 0){
            weaponLabel.textProperty().bind(Bindings.format("%s / %d", ball.getWeapon().getClass().getSimpleName(), ball.getWeapon().ammoProperty()));
        }
        else {
            weaponLabel.textProperty().bind(Bindings.format("%s / *", ball.getWeapon().getClass().getSimpleName()));
        }

    }

    public void bindBallInfo(Ball ball){
        hpBar.progressProperty().bind(Bindings.divide(ball.getHp(), 100));
    }

    public void displayLabel(String labelName, Paint color, double duration, GameObject target, double delay){
        Label label = new Label(labelName);
        label.setTranslateZ(-1);
        label.setTextFill(color);
        label.translateXProperty().bind(Bindings.add(target.getTranslateXProperty(), -labelName.length()*4));
        DoubleProperty yDiffProperty = new SimpleDoubleProperty(-20);
        label.translateYProperty().bind(Bindings.add(target.getTranslateYProperty(), yDiffProperty));

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(duration),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                target.getEnvironment().getDisplay().getChildren().remove(label);
                            }
                        },
                        new KeyValue(yDiffProperty, -50))
        );

        if (delay > 0){
            final Timeline delayTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(delay),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    target.getEnvironment().getDisplay().getChildren().add(label);
                                    timeline.play();
                                }
                            })
            );
            delayTimeline.play();
        }
        else {
            target.getEnvironment().getDisplay().getChildren().add(label);
            timeline.play();
        }

    }

    public void bindBall(Ball ball){
        bindCamera(ball);
        bindBallInfo(ball);
        bindWeaponInfo(ball);
    }

    public void registerBuffState(BuffState buffState){
        Label label = new Label(buffState.getClass().getSimpleName());
        label.setTextFill(buffState.getColor());
        buffBar.getChildren().add(label);
        buffLabels.put(buffState.toString(), label);
    }

    public void removeBuffState(BuffState buffState){
        Label label = buffLabels.get(buffState.toString());
        buffBar.getChildren().remove(label);
        buffLabels.remove(buffState.toString());
    }
}
