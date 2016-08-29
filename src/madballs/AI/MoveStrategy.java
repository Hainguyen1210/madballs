package madballs.AI;

import madballs.GameObject;
import madballs.moveBehaviour.StraightMove;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by caval on 30/08/2016.
 */
public class MoveStrategy extends Strategy {
    private StraightMove straightMove;
    private LinkedList<int[]> path = new LinkedList<>();
    private GameObject objective;
    private double objectiveValue = -1;
    private Random random = new Random();
    private long lastRandomMoveTime = 0;

    public MoveStrategy(BotPlayer bot) {
        super(bot);
        setImportance(6);
    }

    @Override
    public void prepare() {
        straightMove = (StraightMove) getBot().getBall().getMoveBehaviour();
    }

    @Override
    public void consider(GameObject obj) {

    }

    @Override
    public void act() {
        if (path.size() == 0){
            if ((getBot().getLastThoughtTime() - lastRandomMoveTime) / 1000000000 > 3 + random.nextInt(3)){
                lastRandomMoveTime = getBot().getLastThoughtTime();
                straightMove.setVelocityX(straightMove.getSpeed() * (random.nextInt(3) - 1));
                straightMove.setVelocityY(straightMove.getSpeed() * (random.nextInt(3) - 1));
            }
        }
        else {

        }
    }

    @Override
    public void updateImportance() {
        if (objectiveValue == -1){
            setImportance(6);
        }
        setImportance(getImportance() * objectiveValue);
    }
}
