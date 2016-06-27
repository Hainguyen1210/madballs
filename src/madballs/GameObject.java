/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Caval
 */
public abstract class GameObject {
    private Shape hitBox;
    private ImageView image = new ImageView();
    private CollisionEffect collisionEffect;
    private CollisionPassiveBehaviour collisionPassiveBehaviour;
    
    private DoubleProperty translateX = new SimpleDoubleProperty(100);
    private DoubleProperty translateY = new SimpleDoubleProperty(100);
    private double damage;
    private double oldX, oldY;
    private MoveBehaviour moveBehaviour;
    
    public GameObject(Environment environment, double x, double y){
        hitBox = new Circle(25, Paint.valueOf("red"));
        translateX.set(x);
        translateY.set(y);
        hitBox.translateXProperty().bind(translateX);
        hitBox.translateYProperty().bind(translateY);
        image.translateXProperty().bind(translateX);
        image.translateYProperty().bind(translateY);
        environment.registerGameObj(this);
    }

    public double getTranslateX() {
        return translateX.get();
    }

    public double getTranslateY() {
        return translateY.get();
    }
    
    public Shape getHitBox(){
        return hitBox;
    }
    
    public ImageView getImage() {
        return image;
    }
    
    public CollisionEffect getCollisionEffect(){
        return collisionEffect;
    }

    public CollisionPassiveBehaviour getCollisionPassiveBehaviour() {
        return collisionPassiveBehaviour;
    }

    public MoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }

    public void setTranslateX(double newX) {
        translateX.set(newX);
    }

    public void setTranslateY(double newY) {
        translateY.set(newY);
    }

    public void setMoveBehaviour(MoveBehaviour moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }
    
    public void sufferPushBack(){
        translateX.set(oldX);
        translateY.set(oldY);
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and return boolean
     * @param target
     * @return 
     */
    public boolean hasCollidedWith(GameObject target){
       Shape intersect = Shape.intersect(hitBox, target.getHitBox());
       return intersect.getBoundsInLocal().getWidth() != -1;
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and act accordingly
     * @param target 
     */
    public void checkCollisionWith(GameObject target){
       if (hasCollidedWith(target)){
           // let the collision effects affect the two collided objects
           onCollision(target);
           target.onCollision(this);
       } 
    }
    
    public void onCollision(GameObject target){
        collisionEffect.affect(target, damage);
    }
}
