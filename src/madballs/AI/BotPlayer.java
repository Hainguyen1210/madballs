package madballs.AI;

import javafx.animation.AnimationTimer;
import madballs.Environment;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.multiplayer.Data;
import madballs.multiplayer.Server;
import madballs.player.Player;
import madballs.projectiles.Projectile;

import java.util.*;

/**
 * Created by caval on 27/08/2016.
 */
public class BotPlayer extends Player {
    private static ArrayList<BotPlayer> botPlayers = new ArrayList<>();
    private final int THOUGHTS_PER_SECONDS = 20;
    private BotClient botClient = new BotClient();
    private ArrayList<Strategy> strategies = new ArrayList<>();
    private long lastThoughtTime = 0;

    public ArrayList<Strategy> getStrategies() {
        return strategies;
    }

    public long getLastThoughtTime() {
        return lastThoughtTime;
    }

    public static ArrayList<BotPlayer> getBotPlayers() {
        return botPlayers;
    }

    private final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastThoughtTime >= 1000000000 / THOUGHTS_PER_SECONDS){
                prepareStrategies();
                lastThoughtTime = now;
                Environment environment = getBall().getEnvironment();
                int counter = 0;
                for (Integer id: getRelevantObjIDs()){
                    for (Strategy strategy: strategies){
                        GameObject object = environment.getObject(id);
                        if (object != null && object != getBall() && object != getBall().getWeapon()){
                            if (object instanceof Projectile) counter++;
                            strategy.consider(object);
                        }
                    }
                }
//                System.out.println("consider " + counter);

                updateStrategies();
                makeActions();
            }
        }
    };

    private void prepareStrategies(){
        for (Strategy strategy: strategies){
            strategy.prepare();
        }
    }

    private void updateStrategies(){
        for (Strategy strategy: strategies){
            strategy.updateImportance();
        }

        Collections.sort(strategies, Comparator.comparingDouble(strategy -> strategy.getImportance()));
    }

    private void makeActions(){
        for (Strategy strategy: strategies){
            strategy.act();
        }
    }

    public BotClient getBotClient() {
        return botClient;
    }

    public void play(){
        strategies.add(new MoveStrategy(this, getBall().getEnvironment().getMap()));
        animation.start();
    }

    public void stop(){
        animation.stop();
        getRelevantObjIDs().clear();
    }

    public BotPlayer() {
        super(null, false);
        botPlayers.add(this);
        botClient.setLocalPlayer(this);
        strategies.add(new DodgeStrategy(this));
        strategies.add(new AvoidObstacleStrategy(this));
        strategies.add(new AttackStrategy(this));
    }

    @Override
    public void sendData(Data data){
//        System.out.println("bot send " + data.getType());
        Server server = (Server) MadBalls.getMultiplayerHandler();
        server.handleData(this, data);
    }
}
