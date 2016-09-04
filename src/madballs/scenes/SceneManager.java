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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
import madballs.*;
import madballs.buffState.BuffState;
import madballs.multiplayer.AnnouncementData;
import madballs.multiplayer.ScoreData;
import madballs.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author caval
 */
public class SceneManager {
    public static final double NUM_MAP_PARTS = 3;
    public static final Color[] teamColors = new Color[]{Color.BLUE, Color.RED, Color.PINK, Color.GREEN, Color.BROWN, Color.ORANGE, Color.BLACK, Color.PURPLE};
    private static SceneManager instance = new SceneManager();
    private FlowPane gameInfoDisplay;
    private ScrollPane scoreBoardContainer;
    private GridPane scoreBoardGrid;
    private Map<Player, HBox> scoreBoard;
    private VBox announcementBoard;
    private Map<Integer, IntegerProperty> teamScoreBoard = new WeakHashMap<>();
    private HBox teamScoresDisplay;
    private ProgressBar hpBar = new ProgressBar();
    private Label weaponLabel = new Label();
    private HBox buffBar = new HBox(10);
    private Map<String, Label> buffLabels;
    private Label bannerLabel;
    private Pane banner;
    private Rectangle2D primaryScreenBounds;
    private double screenWidth, screenHeight;
    // scale: the ratio of the visual/scene size to the actual game element size.
    // i.e. multiplying an element's size by this scale would give the visual size of the element (the size on scene)
    private DoubleProperty scale = new SimpleDoubleProperty(1);
    // zoomOut: the ratio of how much the game elements have been zoomed out compared to its initial size
    private DoubleProperty zoomOut = new SimpleDoubleProperty(1);
    private PerspectiveCamera camera;

    public Label getBannerLabel() {
        return bannerLabel;
    }

    public Map<Integer, IntegerProperty> getTeamScoreBoard() {
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
//
//    public void setScale(double scale) {
//        this.scale.set(scale);
//    }

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

        bannerLabel = new Label();
        bannerLabel.setFont(new Font(40));
        bannerLabel.setTextFill(Color.RED);
        bannerLabel.translateXProperty().bind(Bindings.divide(Bindings.subtract(MadBalls.getAnimationScene().getWidth(), bannerLabel.widthProperty()), 2));
        banner = new Pane(bannerLabel);
        banner.setVisible(false);
        banner.setTranslateY(200);
//        banner.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
        banner.setPrefWidth(MadBalls.getAnimationScene().getWidth());

        gameInfoDisplay = new FlowPane(hpBar, weaponLabel, buffBar);
        gameInfoDisplay.setAlignment(Pos.CENTER_LEFT);
        gameInfoDisplay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        gameInfoDisplay.setPrefWidth(MadBalls.getAnimationScene().getWidth());
        gameInfoDisplay.setPrefHeight(30);
        gameInfoDisplay.translateYProperty().bind(Bindings.subtract(MadBalls.getAnimationScene().getHeight(), gameInfoDisplay.heightProperty()));
//        gameInfoDisplay.setTranslateY(MadBalls.getMainScene().getHeight() - 30);
//        gameInfoDisplay.setTranslateZ(-1);

        announcementBoard = new VBox(10);
        announcementBoard.setPrefWidth(MadBalls.getAnimationScene().getWidth() / 3);
        announcementBoard.setTranslateX(MadBalls.getAnimationScene().getWidth() * 2 / 3);
        announcementBoard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        announcementBoard.setAlignment(Pos.CENTER);

        teamScoresDisplay = new HBox(20);
        teamScoresDisplay.setPrefHeight(40);
        scoreBoardGrid = new GridPane();
        AnchorPane anchorPane = new AnchorPane(scoreBoardGrid);
        scoreBoardContainer = new ScrollPane(anchorPane);
        scoreBoardContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        scoreBoardContainer.setPrefSize(400, MadBalls.getAnimationScene().getHeight()*2/3);
//        scoreBoardContainer.setTranslateZ(-1);
        scoreBoardContainer.setTranslateX((MadBalls.getAnimationScene().getWidth() - 400)/2);
        scoreBoardContainer.setTranslateY((MadBalls.getAnimationScene().getHeight() - scoreBoardContainer.getPrefHeight())/2);
        scoreBoardContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scoreBoardContainer.setVisible(false);
        reloadScoreBoard();

        root.setTranslateZ(-1);
        root.getChildren().addAll(gameInfoDisplay, scoreBoardContainer, banner, announcementBoard);
    }

    public void announceKill(Integer sourceID, Integer targetID, String weaponImage){
        MadBalls.getMultiplayerHandler().sendData(new AnnouncementData(sourceID, targetID, weaponImage, "kill"));
        Ball source = (Ball) MadBalls.getMainEnvironment().getObject(sourceID);

        Label sourceName = new Label(source.getPlayer().getName());
        sourceName.setFont(new Font(20));
        sourceName.setTextFill(teamColors[source.getPlayer().getTeamNum() - 1]);

        Ball target = (Ball) MadBalls.getMainEnvironment().getObject(targetID);

        Label targetName = new Label(target.getPlayer().getName());
        targetName.setFont(new Font(20));
        targetName.setTextFill(teamColors[target.getPlayer().getTeamNum() - 1]);

        ImageView weaponImageView = new ImageView(ImageGenerator.getInstance().getImage(weaponImage));
        weaponImageView.setFitHeight(10);
        weaponImageView.setPreserveRatio(true);

        HBox announcement = new HBox(10, sourceName, weaponImageView, targetName);
        announcement.setAlignment(Pos.CENTER);

        displayAnnnouncement(announcement);
    }

    public void announceFlag(Integer ballID, Integer flagID, String action){
        MadBalls.getMultiplayerHandler().sendData(new AnnouncementData(ballID, flagID, action, "flag"));
        Ball source = (Ball) MadBalls.getMainEnvironment().getObject(ballID);

        Label ballName = new Label(source.getPlayer().getName());
        ballName.setFont(new Font(20));
        ballName.setTextFill(teamColors[source.getPlayer().getTeamNum() - 1]);

        Label actionLabel = new Label(action);

        Flag flag = (Flag) MadBalls.getMainEnvironment().getObject(flagID);

        Label flagName = new Label(String.format("team %d's flag", flag.getTeamNum()));
        flagName.setFont(new Font(20));
        flagName.setTextFill(teamColors[flag.getTeamNum() - 1]);


        HBox announcement = new HBox(10, ballName, actionLabel, flagName);
        announcement.setAlignment(Pos.CENTER);

        displayAnnnouncement(announcement);
    }

    public void displayAnnnouncement(HBox announcement){
        announcementBoard.getChildren().add(announcement);

        while (announcementBoard.getChildren().size() > 5){
            announcementBoard.getChildren().remove(0);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox vBox = announcementBoard;
                if (vBox.getChildren().contains(announcement)){
                    vBox.getChildren().remove(announcement);
                }
            }
        }));
        timeline.play();
    }

    public void displayBanner(String bannerText, int duration){
        bannerLabel.setText(bannerText);
        banner.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                banner.setVisible(false);
                bannerLabel.textProperty().unbind();
            }
        }));
        timeline.play();
    }

    public void displayCountdown(int duration){
        IntegerProperty counter = new SimpleIntegerProperty(duration);
        bannerLabel.textProperty().bind(Bindings.format("%d", counter));
        banner.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                banner.setVisible(false);
                bannerLabel.textProperty().unbind();
            }
        }, new KeyValue(counter, 0)));
        timeline.play();
    }

    public void resetTeamScoreBoard(){
        teamScoreBoard = new WeakHashMap<>();
    }

    public void reloadTeamScores(){
        teamScoresDisplay.getChildren().clear();
        for (Integer teamNum : teamScoreBoard.keySet()){
            Label score = new Label();
            score.textProperty().bind(Bindings.format("%d", teamScoreBoard.get(teamNum)));
//            System.out.println("score" + teamScoreBoard.get(teamNum));
//            System.out.println(teamNum);
            score.setFont(new Font(20));
            score.setTextFill(teamColors[teamNum-1]);
            teamScoresDisplay.getChildren().add(score);
        }
        teamScoresDisplay.setAlignment(Pos.CENTER);
    }

    public void addScore(int teamNum, int additionalScore){
        teamScoreBoard.get(teamNum).set(teamScoreBoard.get(teamNum).get() + additionalScore);
        if (MadBalls.isHost()) MadBalls.getMultiplayerHandler().sendData(new ScoreData(teamNum, additionalScore));
//        int oldScore = teamScoreBoard.get(teamNum);
////        System.out.println("oldScore" + oldScore);
//        teamScoreBoard.replace(teamNum, oldScore + additionalScore);
////        System.out.println("newScore" + teamScoreBoard.get(teamNum));
//        reloadTeamScores();
    }

    public void reloadScoreBoard(){
        scoreBoard = new WeakHashMap<>();
        for (Player player : MadBalls.getMultiplayerHandler().getPlayers()){
            if (!teamScoreBoard.containsKey(player.getTeamNum())){
                teamScoreBoard.put(player.getTeamNum(), new SimpleIntegerProperty(0));
            }

            HBox playerHBox = new HBox(15);
            Label playerName = new Label(player.getName());
            playerName.setPrefWidth(120);
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
//            System.out.println("reorder"+player.getName() + player.getRanking());
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
//        System.out.println("asd" + MadBalls.getAnimationScene().getHeight());
        scale.bind(Bindings.divide(MadBalls.getAnimationScene().getHeight() * 1.07735026918962555 / MadBalls.getMainEnvironment().getMap().getHeight() * NUM_MAP_PARTS, zoomOut));
//        scale.bind(Bindings.divide(
//                Bindings.divide(MadBalls.getAnimationScene().heightProperty(), MadBalls.getMainEnvironment().getMap().getHeight() / NUM_MAP_PARTS)
//                , zoomOut));
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        // display the map by how many vertical parts it has been divided into and how much it has been zoom out
        // e.g. if the map is 720px high, divided into 3 parts, and zoomed out by 1.5, the displaying height is 720/3*1.5 = 360
        camera.translateZProperty().bind(Bindings.multiply(zoomOut, -MadBalls.getMainEnvironment().getMap().getHeight() / NUM_MAP_PARTS / Math.tan(Math.toRadians(30))));
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
        Label label = new Label(buffState.getName());
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
