/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.gameFX.SoundStudio;

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
    public void uniqueGetAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        SoundStudio.getInstance().playAudio("nutfall", 0.5, this.toString(), target.getEnvironment().getLastUpdateTime(),
                target.getTranslateX(), target.getTranslateY(), 150, 150);
        double pushBackAmount = effect.getPushBackAmount();

        if (pushBackAmount < 0){
            for (int i = 0; i < 1; i ++){
                Shape intersect = collisionShape;
                double intersectWidth = intersect.getBoundsInLocal().getWidth();
                double intersectHeight = intersect.getBoundsInLocal().getHeight();

                double currentDirection = Math.toRadians(target.getRotateAngle());
                double oldX = target.getOwnerOldX();
                double oldY = target.getOwnerOldY();
                target.setRotate(target.getOldDirection());

                if (Shape.intersect(source.getHitBox(), target.getHitBox()).getBoundsInLocal().getWidth() == -1){
                    break;
                }
                else {
                    target.setRotate(currentDirection);
                }
                boolean isXReversed = false;
                if (intersectWidth == intersectHeight){
                    target.setTranslateX(oldX);
                    target.setTranslateY(oldY);
                }
                else {
                    if (intersectWidth < intersectHeight){
                        target.setTranslateX(oldX);
                        isXReversed = true;
                    }
                    else if (intersectWidth > intersectHeight){
                        target.setTranslateY(oldY);
                    }

                    intersect = Shape.intersect(source.getHitBox(), target.getHitBox());
                    if (intersect.getBoundsInLocal().getWidth() != -1 ){
                        if (isXReversed) {
                            target.setTranslateY(oldY);
                        }
                        else {
                            target.setTranslateX(oldX);
                        }
                    }
                }

                if (Shape.intersect(source.getHitBox(), target.getHitBox()).getBoundsInLocal().getWidth() != -1){
                    target.setRotate(target.getOldDirection());
                }
            }
        }
        else if (pushBackAmount > 0){

        }
    }

    @Override
    protected boolean isConditionMet(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        return effect instanceof PushBackEffect;
    }

}
