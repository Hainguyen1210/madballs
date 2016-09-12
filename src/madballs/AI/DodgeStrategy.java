package madballs.AI;

import javafx.geometry.Bounds;
import madballs.GameObject;
import madballs.collision.DamageEffect;
import madballs.collision.ExplosiveBehaviour;
import madballs.collision.StackedCollisionEffect;
import madballs.collision.StackedCollisionPassiveBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.projectiles.Projectile;

/**
 * the strategy to avoid projectiles
 * Created by caval on 28/08/2016.
 */
public class DodgeStrategy extends Strategy {
    private GameObject dangerousObj;
    private double secondsUntilDanger = -1;
    private final double REACTION_TIME_LIMIT = 3;
    private boolean isNoDanger = true;
    private double myX, myY;
    double myRadius = 20;

    public DodgeStrategy(BotPlayer bot) {
        super(bot);
        setImportance(10);
    }

    @Override
    public void prepare() {
        myX = getBot().getBall().getTranslateX();
        myY = getBot().getBall().getTranslateY();

        isNoDanger = true;
        secondsUntilDanger = -1;
    }

    /**
     * calculate how danger the checking GameObject is to the Ball
     * @param object
     */
    private void calculateDanger(GameObject object){
        // check if the object can damage the Ball
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
            double velocityX = objMovement.getVelocityX();
            double velocityY = objMovement.getVelocityY();
            if (velocityX == 0 && velocityY == 0){
                return;
            }

            Bounds objBounds = object.getDisplay().getBoundsInParent();
            double objX = object.getTranslateX();
            double objY = object.getTranslateY();

            // check if the Ball is in range of the Projectile
            if (object instanceof Projectile){
                double distance = Math.sqrt(Math.pow(objX - myX,2) + Math.pow(objY - myY, 2));
                if (objMovement.getMovedDistance() + distance > ((Projectile)object).getSourceWeapon().getRange() + myRadius) {
                    return;
                }
            }

            // check if the projectile will hit the Ball

            if (Math.abs(velocityY) >= Math.abs(velocityX)){
                double deltaTime = (myY - objY) / velocityY;
                if (isDeltaTimeDangerous(deltaTime)){
                    double objLeftWidth = objX - objBounds.getMinX();
                    double objRightWidth = objBounds.getMaxX() - objX;
                    double objExpectedX = objX + velocityX * deltaTime;
                    if ((objExpectedX - objLeftWidth >= myX - myRadius && objExpectedX - objLeftWidth <= myX + myRadius)
                            || (objExpectedX + objRightWidth >= myX - myRadius && objExpectedX + objRightWidth <= myX + myRadius)) {
                        updateDangerObj(object, deltaTime);
                    }
                }
            }
            else if (Math.abs(velocityX) > Math.abs(velocityY)){
                double deltaTime = (myX - objX) / velocityX;
                if (isDeltaTimeDangerous(deltaTime)){
                    double objTopHeight = objY - objBounds.getMinY();
                    double objBottomHeight = objBounds.getMaxY() - objY;
                    double objExpectedY = objY + velocityY * deltaTime;
                    if ((objExpectedY - objTopHeight >= myY - myRadius && objExpectedY - objTopHeight <= myY + myRadius)
                            || (objExpectedY + objBottomHeight >= myY - myRadius && objExpectedY + objBottomHeight <= myY + myRadius)){
                        updateDangerObj(object, deltaTime);
                    }
                }
            }
        }
        else {
            return;
        }
    }

    /**
     * save the danger object
     * @param object
     * @param deltaTime
     */
    private void updateDangerObj(GameObject object, double deltaTime){
        if (object == dangerousObj) setImportance(getImportance() + 1);
        dangerousObj = object;
        secondsUntilDanger = deltaTime;
        isNoDanger= false;
    }

    /**
     * check if the deltaTime until collision is dangerous and needed to be dodge
     * @param deltaTime
     * @return
     */
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
        if (isNoDanger) {
            dangerousObj = null;
            setImportance(10);
        }
        StraightMove straightMove = ((StraightMove)getBot().getBall().getMoveBehaviour());
        if (dangerousObj != null){
            // calculate the direction to dodge the dangerous obj
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

            // dodge the dangerous obj
            double speed = straightMove.getSpeed();

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
    }

    @Override
    public void updateImportance() {
        setImportance(getImportance() / secondsUntilDanger);
    }
}
