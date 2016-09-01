package madballs.collision;

import javafx.scene.paint.Material;
import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.moveBehaviour.StraightMove;
import madballs.projectiles.Projectile;

/**
 * Created by caval on 31/08/2016.
 */
public class DeflectiveBehaviour extends StackedCollisionPassiveBehaviour {
    public DeflectiveBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }

    @Override
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
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

        System.out.println("deflect");
        System.out.println(myAngle);
        System.out.println(incomingAngle);

        double deflectionAngle = (myAngle - incomingAngle) * 2;
        if (deflectionAngle > 180){
            deflectionAngle -= 360;
        }
        else if (deflectionAngle < -180){
            deflectionAngle += 360;
        }

        incomingMovement.setNewDirection(Math.toRadians(incomingAngle + deflectionAngle));
        source.setRotate(Math.toRadians(source.getRotateAngle() + deflectionAngle));

    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return source.getMoveBehaviour() != null && source.getMoveBehaviour() instanceof StraightMove;
    }
}
