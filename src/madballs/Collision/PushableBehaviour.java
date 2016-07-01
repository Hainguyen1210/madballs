/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.Projectiles.Projectile;
import madballs.RotateBehaviour;

/**
 *
 * @author Caval
 */
public class PushableBehaviour extends StackedCollisionPassiveBehaviour{
    
    private double collidedDirection;

    public double getCollidedDirection() {
        return collidedDirection;
    }
    
    public PushableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        super.getAffected(source, target, effect, collisionShape);
        if (effect.hasCollisionEffect(PushBackEffect.class)){
            double pushBackAmount = effect.getPushBackAmount();

            GameObject collidedTarget = target;
            while (target.getOwner() != null){
                target = target.getOwner();
            }

            if (pushBackAmount < 0){
                if (collidedTarget instanceof Projectile) System.out.println(source.getClass());
                Shape intersect = collisionShape;
                double intersectWidth = intersect.getBoundsInLocal().getWidth();
                double intersectHeight = intersect.getBoundsInLocal().getHeight();
                double currentDirection = Math.toRadians(target.getRotateAngle());
                target.setRotate(target.getOldDirection());
//                target.setTranslateX(target.getOldX());
//                target.setTranslateY(target.getOldY());
//                if (collidedTarget.getMoveBehaviour() instanceof RotateBehaviour){
////                    double pushBackDirection = Math.atan2(, target.getTranslateX() - originTarget.getTranslateX());
//                    double pushBackedX = target.getTranslateX() + 1 * (Math.abs(target.getRotateAngle()) > 90 ? 1 : -1);
//                    double pushBackedY = target.getTranslateY() + 1 * (target.getRotateAngle() < 0 ? 1 : -1);
//                    
////                    System.out.println(3 * ((target.getTranslateY() > collidedTarget.getTranslateY()) ? 1 : -1));
////                    System.out.println(Math.sin(pushBackDirection) * 5);
////                    target.setOldX(pushBackedX);
////                    target.setOldY(pushBackedY);
////                    target.setTranslateX(pushBackedX);
////                    target.setTranslateY(pushBackedY);
//                    
//                    while (intersect.getBoundsInLocal().getWidth() != -1){
//                        pushBackedX += Math.abs(target.getRotateAngle()) > 90 ? 1 : -1;
//                        pushBackedY += target.getRotateAngle() < 0 ? 1 : -1;
//                        target.setOldX(pushBackedX);
//                        target.setOldY(pushBackedY);
//                        target.setTranslateX(pushBackedX);
//                        target.setTranslateY(pushBackedY);
//                        
//                        intersect = Shape.intersect(source.getHitBox(), collidedTarget.getHitBox());
//                    }
//                    return;
//                }
                if (Shape.intersect(source.getHitBox(), collidedTarget.getHitBox()).getBoundsInLocal().getWidth() == -1){
                    return;
                }
                else {
                    target.setRotate(currentDirection);
                }
                boolean isXReversed = false;
                if (intersectWidth < intersectHeight){
                    target.setTranslateX(target.getOwnerOldX());
                    isXReversed = true;
                }
                else if (intersectWidth > intersectHeight){
                    target.setTranslateY(target.getOwnerOldY());
                }
                else {
                    target.setTranslateX(target.getOwnerOldX());
                    target.setTranslateY(target.getOwnerOldY());
                }
                
                intersect = Shape.intersect(source.getHitBox(), target.getHitBox());
                if (intersect.getBoundsInLocal().getWidth() != -1 ){
                    if (isXReversed) {
                        target.setTranslateY(target.getOwnerOldY());
                    }
                    else {
                        target.setTranslateX(target.getOwnerOldX());
                    }
                }
                
                if (Shape.intersect(source.getHitBox(), collidedTarget.getHitBox()).getBoundsInLocal().getWidth() != -1){
                    target.setRotate(target.getOldDirection());
                }
//                // check if there is still intersection after reversing the translateX
//                target.setTranslateX(target.getOldX());
//                if (intersect.getBoundsInLocal().getWidth() == -1 ) return;
//                
//                // check if there is still intersection after reversing the translateY
//                target.setTranslateX(newX);
//                target.setTranslateY(target.getOldY());
//                intersect = Shape.intersect(source.getHitBox(), target.getHitBox());
//                if (intersect.getBoundsInLocal().getWidth() == -1) return;
//                
//                // reverse both X, Y
//                target.setTranslateX(target.getOldX());
            }
            else if (pushBackAmount > 0){
                
            }
        }
    }
    
}
