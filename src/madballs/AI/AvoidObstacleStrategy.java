package madballs.AI;

import javafx.geometry.Bounds;
import madballs.GameObject;
import madballs.collision.PushBackEffect;
import madballs.collision.StackedCollisionEffect;
import madballs.moveBehaviour.StraightMove;
import madballs.player.Player;

/**
 * Created by caval on 29/08/2016.
 */
public class AvoidObstacleStrategy extends Strategy {
    private double myX, myY, velocityX, velocityY;

    public AvoidObstacleStrategy(Player player) {
        super(player);
    }

    @Override
    public void prepare() {
        StraightMove straightMove = (StraightMove) getPlayer().getBall().getMoveBehaviour();
        myX = getPlayer().getBall().getTranslateX();
        myY = getPlayer().getBall().getTranslateY();
        velocityX = straightMove.getVelocityX();
        velocityY = straightMove.getVelocityY();
    }

    @Override
    public void consider(GameObject obj) {
        if (velocityX == 0 && velocityY == 0 )return;
        if (obj.getCollisionEffect() != null && ((StackedCollisionEffect)obj.getCollisionEffect()).hasCollisionEffect(PushBackEffect.class)){
            Bounds objBounds = obj.getDisplay().getBoundsInParent();
            double objX = obj.getTranslateX();
            double objLeftWidth = objX - objBounds.getMinX();
            double objRightWidth = objBounds.getMaxX() - objX;
            double objY = obj.getTranslateY();
            double objTopHeight = objY - objBounds.getMinY();
            double objBottomHeight = objBounds.getMaxY() - objY;

            if (velocityY >= velocityX){
                double deltaTime = (objY - myY) / velocityY;
            }
        }
    }

    @Override
    public void act() {

    }

    @Override
    public void updateImportance() {

    }
}
