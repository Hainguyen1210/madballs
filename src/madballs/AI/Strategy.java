package madballs.AI;

import madballs.Ball;
import madballs.GameObject;
import madballs.player.Player;

import java.util.ArrayList;

/**
 * Created by caval on 28/08/2016.
 */
public abstract class Strategy {
    private Player player;
    private double importance = 0;

    public Player getPlayer() {
        return player;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public void raiseImportance(double amount){
        importance += amount;
    }

    public Strategy(Player player) {
        this.player = player;
    }

    public abstract void prepare();
    public abstract void consider(GameObject obj);
    public abstract void act();
    public abstract void updateImportance();
}
