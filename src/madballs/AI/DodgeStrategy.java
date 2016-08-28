package madballs.AI;

import javafx.geometry.Bounds;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.collision.DamageEffect;
import madballs.collision.ExplosiveBehaviour;
import madballs.collision.StackedCollisionEffect;
import madballs.collision.StackedCollisionPassiveBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.player.Player;

/**
 * Created by caval on 28/08/2016.
 */
public class DodgeStrategy extends Strategy {
    private GameObject dangerousObj;
    private double secondsUntilDanger = -1;
    private final double REACTION_TIME_LIMIT = 5;
    private double myX, myY;
    double myRadius = 20;

    public DodgeStrategy(Player player) {
        super(player);
    }

    @Override
    public void prepare() {
        myX = getPlayer().getBall().getTranslateX();
        myY = getPlayer().getBall().getTranslateY();

        dangerousObj = null;
        secondsUntilDanger = -1;
    }

    private void calculateDanger(GameObject object){
        if (object.getCollisionEffect() == null
                || (!((StackedCollisionEffect)object.getCollisionEffect()).hasCollisionEffect(DamageEffect.class)
                    && !((StackedCollisionPassiveBehaviour)object.getCollisionPassiveBehaviour()).hasCollisionBehaviour(ExplosiveBehaviour.class))){
            return;
        }
        if (object.getMoveBehaviour() == null){
            return;
        }
        if (object.getMoveBehaviour() instanceof StraightMove){
            StraightMove objMovement = (StraightMove) object.getMoveBehaviour();
            double velocityX= objMovement.getVelocityX();
            double velocityY = objMovement.getVelocityY();
            if (velocityX == 0 && velocityY == 0){
                return;
            }

            Bounds objBounds = object.getDisplay().getBoundsInParent();
            double objX = object.getTranslateX();
            double objLeftWidth = objX - objBounds.getMinX();
            double objRightWidth = objBounds.getMaxX() - objX;
            double objY = object.getTranslateY();
            double objTopHeight = objY - objBounds.getMinY();
            double objBottomHeight = objBounds.getMaxY() - objY;

            if (Math.abs(velocityY) >= Math.abs(velocityX)){
                double deltaTime = (myY - objY) / velocityY;
                if (isDeltaTimeDangerous(deltaTime)){
                    double objExpectedX = objX + velocityX * deltaTime;
                    if ((objExpectedX - objLeftWidth >= myX - myRadius && objExpectedX - objLeftWidth <= myX + myRadius)
                            || (objExpectedX + objRightWidth >= myX - myRadius && objExpectedX + objRightWidth <= myX + myRadius)) {
                        dangerousObj = object;
                        secondsUntilDanger = deltaTime;
                    }
                }
            }
            else if (Math.abs(velocityX) > Math.abs(velocityY)){
                double deltaTime = (myX - objX) / velocityX;
                if (isDeltaTimeDangerous(deltaTime)){
                    double objExpectedY = objY + velocityY * deltaTime;
                    if ((objExpectedY - objTopHeight >= myY - myRadius && objExpectedY - objTopHeight <= myY + myRadius)
                            || (objExpectedY + objBottomHeight >= myY - myRadius && objExpectedY + objBottomHeight <= myY + myRadius)){
                        dangerousObj = object;
                        secondsUntilDanger = deltaTime;
                    }
                }
            }
        }
        else {
            return;
        }
    }

    private boolean isDeltaTimeDangerous(double deltaTime){
        if (deltaTime < 0 || deltaTime > REACTION_TIME_LIMIT) {
            return false;
        }
        else if (secondsUntilDanger != -1 && deltaTime > secondsUntilDanger){
            return false;
        }
        return true;
    }

    @Override
    public void consider(GameObject obj) {
        calculateDanger(obj);
    }

    @Override
    public void act() {
        StraightMove straightMove = ((StraightMove)getPlayer().getBall().getMoveBehaviour());
        if (dangerousObj != null){
//            double myX = getPlayer().getBall().getTranslateX();
//            double myY = getPlayer().getBall().getTranslateY();
            double dangerX = dangerousObj.getTranslateX();
            double dangerY = dangerousObj.getTranslateY();
            double angleFromDanger = Math.toDegrees(Math.atan2(myY - dangerY, myX - dangerX));
            double dangerMovementAngle = Math.toDegrees(((StraightMove)dangerousObj.getMoveBehaviour()).getNewDirection());
            double dodgeAngle;

            if (Math.abs(dangerMovementAngle) <= 90){
                if (Math.signum(angleFromDanger) == Math.signum(dangerMovementAngle)){
                    dodgeAngle = 90 * Math.signum(angleFromDanger - dangerMovementAngle) + dangerMovementAngle;
                }
                else {
                    dodgeAngle = (90 - dangerMovementAngle) * -Math.signum(dangerMovementAngle);
                }
            }
            else {
                dodgeAngle = 90 * (Math.signum(angleFromDanger) == Math.signum(dangerMovementAngle) ? 1 : -1) * Math.signum(angleFromDanger - dangerMovementAngle) + dangerMovementAngle;
            }
            if (dodgeAngle > 180){
                dodgeAngle -= 360;
            }
            if (dodgeAngle < -180){
                dodgeAngle += 360;
            }
            System.out.println("avoid danger" + dodgeAngle);
            System.out.println(angleFromDanger);
            System.out.println(dangerMovementAngle);
            double speed = straightMove.getSpeed();
//            if (dodgeAngle > 180){
//                dodgeAngle -= 360;
//            }
            if (Math.abs(dodgeAngle) >= 30 && Math.abs(dodgeAngle) <= 150){
                straightMove.setVelocityY(speed * Math.signum(dodgeAngle));
            }
            if (Math.abs(dodgeAngle) <= 60){
                straightMove.setVelocityX(speed);
            }
            else if (Math.abs(dodgeAngle) >= 120){
                straightMove.setVelocityX(-speed);
            }
        }
        else {
            straightMove.setVelocityX(0);
            straightMove.setVelocityY(0);
        }
    }

    @Override
    public void updateImportance() {
        setImportance(10 / secondsUntilDanger);
    }
}
