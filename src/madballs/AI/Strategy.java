package madballs.AI;

import madballs.Ball;
import madballs.GameObject;
import madballs.player.Player;

import java.util.ArrayList;

/**
 * Created by caval on 28/08/2016.
 */
public abstract class Strategy {
    private BotPlayer bot;
    private double importance = 0;

    public BotPlayer getBot() {
        return bot;
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

    public Strategy(BotPlayer bot) {
        this.bot = bot;
    }

    /**
     * prepare the required properties needed to process the strategy
     */
    public abstract void prepare();

    /**
     * consider how a GameObject affect this strategy
     * @param obj
     */
    public abstract void consider(GameObject obj);

    /**
     * make appropriate actions
     */
    public abstract void act();

    /**
     * update how important this strategy is
     */
    public abstract void updateImportance();
}
