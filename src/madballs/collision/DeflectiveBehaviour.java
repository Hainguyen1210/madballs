package madballs.collision;

import javafx.scene.paint.Material;
import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.moveBehaviour.StraightMove;
import madballs.projectiles.Projectile;

/**
 * the GameObject with this behaviour can deflect the straight movement of the GameObject colliding it
 * Created by caval on 31/08/2016.
 */
public class DeflectiveBehaviour extends StackedCollisionPassiveBehaviour {
    private boolean shouldResetDistance = false;

    public DeflectiveBehaviour(boolean shouldResetDistance, StackedCollisionPassiveBehaviour behaviour) {
        super(behaviour);
        this.shouldResetDistance = shouldResetDistance;
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        if (source.getMoveBehaviour() instanceof  StraightMove){
            StraightMove incomingMovement = (StraightMove) source.getMoveBehaviour();

            double myAngle = target.getRotateAngle();
            if (myAngle > 180) {
                myAngle -= 360;
            }
            else if (myAngle < -180) {
                myAngle += 360;
            }

            double incomingAngle = Math.toDegrees(incomingMovement.getNewDirection());
            incomingAngle = (180 - Math.abs(incomingAngle)) * -Math.signum(incomingAngle);
            if (incomingAngle > 180){
                incomingAngle -= 360;
            }
            else if (incomingAngle < - 180){
                incomingAngle += 360;
            }

            double deflectionAngle = (myAngle - incomingAngle) * 2;
            if (deflectionAngle > 180){
                deflectionAngle -= 360;
            }
            else if (deflectionAngle < -180){
                deflectionAngle += 360;
            }

            incomingMovement.setNewDirection(Math.toRadians(incomingAngle + deflectionAngle));
            source.setRotate(Math.toRadians(source.getRotateAngle() + deflectionAngle));

            if (shouldResetDistance) incomingMovement.setMovedDistance(0);
        }


    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return source.getMoveBehaviour() != null && source.getMoveBehaviour() instanceof StraightMove;
    }
}
