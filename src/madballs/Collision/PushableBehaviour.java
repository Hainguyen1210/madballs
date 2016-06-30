/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Collision;

import javafx.scene.shape.Shape;
import madballs.GameObject;
import madballs.RotateBehaviour;

/**
 *
 * @author Caval
 */
public class PushableBehaviour extends StackedCollisionPassiveBehaviour{
    
    public PushableBehaviour(CollisionPassiveBehaviour behaviour) {
        super(behaviour);
    }
    
    @Override
    public void getAffected(GameObject source, GameObject target, StackedCollisionEffect effect, Shape collisionShape) {
        super.getAffected(source, target, effect, collisionShape);
        if (effect.hasCollisionEffect(PushBackEffect.class)){
            double pushBackAmount = effect.getPushBackAmount();
            if (pushBackAmount < 0){
                System.out.println("suffer");
                Shape intersect = collisionShape;
                double intersectWidth = intersect.getBoundsInLocal().getWidth();
                double intersectHeight = intersect.getBoundsInLocal().getHeight();
                
                if (target.getMoveBehaviour() instanceof RotateBehaviour){
                    target.setRotate(target.getMoveBehaviour().getOldDirection());
                }
                
                while (target.getOwner() != null){
                    target = target.getOwner();
                }
                
                if (intersectWidth < intersectHeight){
                    target.setTranslateX(target.getOldX());
//                    isXReversed = true;
                }
                else if (intersectWidth > intersectHeight){
                    target.setTranslateY(target.getOldY());
                }
                else {
                    target.setTranslateX(target.getOldX());
                    target.setTranslateY(target.getOldY());
                }
                
//                intersect = Shape.intersect(source.getHitBox(), target.getHitBox());
//                if (intersect.getBoundsInLocal().getWidth() != -1 ){
//                    if (isXReversed) {
//                        target.setTranslateY(target.getOldY());
//                    }
//                    else {
//                        target.setTranslateX(target.getOldX());
//                    }
//                }
                
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
