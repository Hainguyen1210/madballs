/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import madballs.Collision.CollisionPassiveBehaviour;
import madballs.Collision.CollisionEffect;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Caval
 */
public abstract class GameObject {
    private Shape hitBox;
    private ImageView image = new ImageView();
    private Group display = new Group(hitBox, image);
    private CollisionEffect collisionEffect;
    private CollisionPassiveBehaviour collisionPassiveBehaviour;
    
    private DoubleProperty translateX = new SimpleDoubleProperty(100);
    private DoubleProperty translateY = new SimpleDoubleProperty(100);
    private double oldX, oldY;
    private double minX, minY, maxX, maxY;
    private DoubleProperty hp = new SimpleDoubleProperty(100);
    private MoveBehaviour moveBehaviour;
    private Environment environment;
    
    public GameObject(Environment environment, double x, double y){
        calculateBoundaries();
        translateX.set(x);
        translateY.set(y);
        display.translateXProperty().bind(translateX);
        display.translateYProperty().bind(translateY);
        
        this.environment = environment;
        environment.registerGameObj(this, true);
    }
    
    /**
     * create a new GameObject inside a created Obj
     * @param owner 
     */
    public GameObject(GameObject owner){
        display.translateXProperty().bind(owner.translateX);
        display.translateYProperty().bind(owner.translateY);
        owner.display.getChildren().add(display);
        
        environment.registerGameObj(this, false);
    }
    
    /**
     * calculate the min and max coordinates that the obj can reach
     */
    private void calculateBoundaries(){
        if (hitBox instanceof Circle){
            double radius = ((Circle) hitBox).getRadius();
            minX = radius;
            minY = radius;
            maxX = MadBalls.RESOLUTION_X - radius;
            maxY = MadBalls.RESOLUTION_Y - radius;
        }
        else if (hitBox instanceof Rectangle){
            minX = 0;
            minY = 0;
            maxX = MadBalls.RESOLUTION_X - ((Rectangle)hitBox).getWidth();
            maxY = MadBalls.RESOLUTION_Y - ((Rectangle)hitBox).getHeight();
        }
    }

    public Environment getEnvironment() {
        return environment;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
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

    public Group getDisplay() {
        return display;
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

    public double getHp() {
        return hp.get();
    }

    public void setHitBox(Shape hitBox) {
        this.hitBox = hitBox;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setHp(double hp) {
        this.hp.set(hp);
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

    public void setCollisionEffect(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    public void setCollisionPassiveBehaviour(CollisionPassiveBehaviour collisionPassiveBehaviour) {
        this.collisionPassiveBehaviour = collisionPassiveBehaviour;
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
        collisionEffect.affect(this, target);
    }
    
    public abstract void update(long now);
}
