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

        // check if weapon can attack
        weapon = getBot().getBall().getWeapon();
        if (weapon.getProjectileCollisionEffect() == null || weapon.getProjectileCollisionBehaviour() == null) {
            weapon = null;
            return;
        }
        if (((StackedCollisionEffect)weapon.getProjectileCollisionEffect()).hasCollisionEffect(DamageEffect.class)
                || ((StackedCollisionPassiveBehaviour)weapon.getProjectileCollisionBehaviour()).hasCollisionBehaviour(ExplosiveBehaviour.class)) {
            // save weapon current properties
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

            // prevent attacking allies and healing enemies
            if ((weapon.getDamage() >= 0 && target.getPlayer().getTeamNum() == getBot().getTeamNum())
                    || (weapon.getDamage() < 0 && target.getPlayer().getTeamNum() != getBot().getTeamNum())){
                return;
            }

            // get the coordinates to fire at
            double targetX = target.getTranslateX();
            double targetY = target.getTranslateY();

            // predicting aim mode: aim the target at where it is going to move to
            // e.g. target A is moving to spot B, then fire at spot B
            if (currentAimMode == 0){
                StraightMove targetMovement = (StraightMove) target.getMoveBehaviour();
                double targetSpeed = Math.sqrt(Math.pow(targetMovement.getVelocityX(), 2) + Math.pow(targetMovement.getVelocityY(), 2));

                double baseDiffY = target.getTranslateY() - weaponY;
                double baseDiffX = target.getTranslateX() - weaponX;
                double baseSquaredDistance = baseDiffX * baseDiffX + baseDiffY * baseDiffY;  // distance to target

                double deltaTime = Math.sqrt(baseSquaredDistance / (targetSpeed * targetSpeed + projectileSpeed * projectileSpeed));  // time until the projectile hit spot B

                // spot B coordinates
                targetX += targetMovement.getVelocityX() * deltaTime;
                targetY += targetMovement.getVelocityY() * deltaTime;
            }

            // calculate distance and angle to target
            double diffX = targetX - weaponX;
            double diffY = targetY - weaponY;
            double distance = Math.sqrt(diffX * diffX + diffY * diffY);
            double angle = Math.toDegrees(Math.atan2(diffY, diffX));

            // allow the Bot to fire TrapLauncher at close range
            if (weapon instanceof  TrapLauncher){
                distance -= 35;
            }

            // calculate if the target is in range
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
                        // skip the other enemy Balls when the weapon deals damage (when it is OK to mis-hit other enemies)
                        // skip the other ally Balls when the weapon heals health (when it is OK to mis-hit other allies)
                        if (checkingObj instanceof Ball){
                            if (checkingObj == getBot().getBall()
                                    || (((Ball) checkingObj).getPlayer().getTeamNum() != getBot().getTeamNum() && weapon.getDamage() >= 0)
                                    || (((Ball) checkingObj).getPlayer().getTeamNum() == getBot().getTeamNum() && weapon.getDamage() < 0)){
                                continue;
                            }
                        }
                        // does not check objects other than Balls or Obstacles
                        else if (!(checkingObj instanceof Obstacle)){
                            continue;
                        }

                        /* ********* actually check if the obj will block the projectile ******* */

                        // check if the checking obj is on the direction of the projectile

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

                        // check if the distance to the checking object is less than the distance to the target
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
                                // if the checking obj gets into this 'if' block -> the obj is blocking the projectile from the target
                                return;
                            }
                        }

                    }
                }
            }
            // for point & click weapons
            else {
                weapon.setRange(distance);
            }

            // save the valid target
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
                // record the initial aiming
                initialAimTime = getBot().getLastThoughtTime();
                initialHp = target.getHpValue();
                aimedTarget = target;
            }
            if (target == aimedTarget){
                if ((getBot().getLastThoughtTime() - initialAimTime) / 1000000000 > 4 + (new Random()).nextInt(3)){
                    if (target.getHpValue() == initialHp) {
                        // if it has been a while since the Bot started aiming and the target has not lost any HP, then change the aiming mode
                        currentAimMode = currentAimMode == 0 ? 1 : 0;
                    }
                    // update the aiming status
                    initialAimTime = getBot().getLastThoughtTime();
                    initialHp = target.getHpValue();
                }
            }

            // aim and fire
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
