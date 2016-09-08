package madballs.AI;

import javafx.geometry.Bounds;
import madballs.Ball;
import madballs.GameObject;
import madballs.Obstacle;
import madballs.collision.DamageEffect;
import madballs.collision.ExplosiveBehaviour;
import madballs.collision.StackedCollisionEffect;
import madballs.collision.StackedCollisionPassiveBehaviour;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Shield;
import madballs.wearables.TrapLauncher;
import madballs.wearables.Weapon;

import java.util.Random;

/**
 * Created by caval on 29/08/2016.
 */
public class AttackStrategy extends Strategy {
    private Ball target, aimedTarget;
    private int currentAimMode = 0;
    private double initialHp;
    private long initialAimTime;
    private double angleToEnemy = 0;
    private Weapon weapon;
    private double weaponX, weaponY, projectileSpeed, projectileSize;

    public AttackStrategy(BotPlayer bot) {
        super(bot);
        setImportance(8);
    }

    @Override
    public void prepare() {
        target = null;
        angleToEnemy = 0;
        weapon = getBot().getBall().getWeapon();
        if (weapon.getProjectileCollisionEffect() == null || weapon.getProjectileCollisionBehaviour() == null) {
            weapon = null;
            return;
        }
        if (((StackedCollisionEffect)weapon.getProjectileCollisionEffect()).hasCollisionEffect(DamageEffect.class)
                || ((StackedCollisionPassiveBehaviour)weapon.getProjectileCollisionBehaviour()).hasCollisionBehaviour(ExplosiveBehaviour.class)) {
            double[] realCoordinates = weapon.getRealCoordinate();
            weaponX = realCoordinates[0];
            weaponY = realCoordinates[1];
            projectileSpeed = weapon.getProjectileSpeed();
            projectileSize = weapon.getProjectileHitBoxSize();
        }
        else {
            weapon = null;
        }
    }

    @Override
    public void consider(GameObject obj) {
        if (weapon == null) return;
        if (obj instanceof Ball){
            // find Ball target
            Ball target = (Ball) obj;
            if (this.target != null && this.target.getHpValue() <= target.getHpValue()){
                return;
            }
            if ((weapon.getDamage() >= 0 && target.getPlayer().getTeamNum() == getBot().getTeamNum())
                    || (weapon.getDamage() < 0 && target.getPlayer().getTeamNum() != getBot().getTeamNum())){
                return;
            }

            // calculate target shot coordinates
            double targetX = target.getTranslateX();
            double targetY = target.getTranslateY();
            if (currentAimMode == 0){
                StraightMove targetMovement = (StraightMove) target.getMoveBehaviour();
                double targetSpeed = Math.sqrt(Math.pow(targetMovement.getVelocityX(), 2) + Math.pow(targetMovement.getVelocityY(), 2));
                double baseDiffY = target.getTranslateY() - weaponY;
                double baseDiffX = target.getTranslateX() - weaponX;
                double baseSquaredDistance = baseDiffX * baseDiffX + baseDiffY * baseDiffY;
                double deltaTime = Math.sqrt(baseSquaredDistance / (targetSpeed * targetSpeed + projectileSpeed * projectileSpeed));
                targetX += targetMovement.getVelocityX() * deltaTime;
                targetY += targetMovement.getVelocityY() * deltaTime;
            }


            double diffX = targetX - weaponX;
            double diffY = targetY - weaponY;
            double distance = Math.sqrt(diffX * diffX + diffY * diffY);
            double angle = Math.toDegrees(Math.atan2(diffY, diffX));
            if (weapon instanceof  TrapLauncher){
                distance -= 35;
            }
            if (weapon.getMaxRange() == -1){
                if (distance > weapon.getRange()) return;
            }
            else if (distance > weapon.getMaxRange()) {
                return;
            }

            // check if there is any blocking objects
            if (weapon.getMaxRange() == -1){
                for (Integer id: getBot().getRelevantObjIDs()){
                    GameObject checkingObj = weapon.getEnvironment().getObject(id);
                    if (checkingObj != null) {
                        if (checkingObj instanceof Ball){
                            if (checkingObj == getBot().getBall()
                                    || (((Ball) checkingObj).getPlayer().getTeamNum() != getBot().getTeamNum() && weapon.getDamage() >= 0)
                                    || (((Ball) checkingObj).getPlayer().getTeamNum() == getBot().getTeamNum() && weapon.getDamage() < 0)){
                                continue;
                            }
                        }
                        else if (!(checkingObj instanceof Obstacle)){
                            continue;
                        }


                        Bounds checkingBounds = checkingObj.getDisplay().getBoundsInParent();
                        double checkingMinDiffY = checkingBounds.getMinY() - weaponY;
                        double checkingMaxDiffY = checkingBounds.getMaxY() - weaponY;
                        double checkingMinX, checkingMaxX;
                        if (Math.signum(diffX) == Math.signum(diffY)){
                            checkingMinX = checkingBounds.getMaxX();
                            checkingMaxX = checkingBounds.getMinX();
                        }
                        else {
                            checkingMinX = checkingBounds.getMinX();
                            checkingMaxX = checkingBounds.getMaxX();
                        }
                        double checkingMinDiffX = checkingMinX - weaponX;
                        double checkingMaxDiffX = checkingMaxX - weaponX;
                        double checkingMinAngle = Math.toDegrees(Math.atan2(checkingMinDiffY, checkingMinDiffX));
                        double checkingMaxAngle = Math.toDegrees(Math.atan2(checkingMaxDiffY, checkingMaxDiffX));

                        if (Math.abs(checkingMaxAngle - checkingMinAngle) > 180){
                            if (checkingMaxAngle < 0) {
                                checkingMaxAngle += 360;
                            }
                            else {
                                checkingMinAngle += 360;
                            }
                            if (angle < 0) angle += 360;
                        }
                        else {
                            if (angle > 180) angle -= 360;
                        }
                        if ((angle <= checkingMaxAngle && angle >= checkingMinAngle)
                                || (angle >= checkingMaxAngle && angle <= checkingMinAngle)){

                            double checkingDistance;
                            double intersectX = 0, intersectY = 0;
                            if (diffX != 0){
                                double projectileLineSlope = diffY / diffX;
                                double projectileLineIntercept = targetY - targetX * projectileLineSlope;
                                double checkingLineSlope = (checkingBounds.getHeight()) / (checkingMaxX - checkingMinX);
                                double checkingLineIntercept = checkingBounds.getMaxY() - checkingMaxX * checkingLineSlope;
                                intersectX = (checkingLineIntercept - projectileLineIntercept) / (projectileLineSlope - checkingLineSlope);
                                intersectY = intersectX * projectileLineSlope + projectileLineIntercept;
                                checkingDistance = Math.sqrt(Math.pow(intersectY - weaponY, 2) + Math.pow(intersectX - weaponX, 2));
                            }
                            else {
                                checkingDistance = Math.abs((checkingBounds.getMaxY() + checkingBounds.getMinY()) / 2 - weaponY);
                            }

                            if (checkingDistance < distance){

                                return;
                            }
                        }

                    }
                }
            }
            else {
                weapon.setRange(distance);
            }

            this.target = target;
            angleToEnemy = Math.toDegrees(Math.atan2(diffY, diffX));
        }
    }

    @Override
    public void act() {
        if (weapon == null) return;
        RotateBehaviour rotateBehaviour = (RotateBehaviour) weapon.getMoveBehaviour();
        if (target != null){
            if (aimedTarget == null || target != aimedTarget) {
                initialAimTime = getBot().getLastThoughtTime();
                initialHp = target.getHpValue();
                aimedTarget = target;
            }
            if (target == aimedTarget){
                if ((getBot().getLastThoughtTime() - initialAimTime) / 1000000000 > 4 + (new Random()).nextInt(3)){
                    if (target.getHpValue() == initialHp) currentAimMode = currentAimMode == 0 ? 1 : 0;
                    initialAimTime = getBot().getLastThoughtTime();
                    initialHp = target.getHpValue();
                }
            }
            rotateBehaviour.setNewDirection(Math.toRadians(angleToEnemy));
            rotateBehaviour.setMousePressed(true);
        }
        else {
            rotateBehaviour.setMousePressed(false);
        }
    }

    @Override
    public void updateImportance() {
        if (target == null){
            setImportance(0);
        }
        else {
            setImportance(getImportance() / (target.getHpValue() / 10));
        }
    }
}
