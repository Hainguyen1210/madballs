/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.beans.binding.Bindings;
import madballs.Collision.CollisionPassiveBehaviour;
import madballs.Collision.CollisionEffect;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Caval
 */
public abstract class GameObject {
    private GameObject owner, child;
    private Shape hitBox;
    private ImageView imageView = new ImageView();
    private Group display;
    private Rectangle boundsRectangle;
    private CollisionEffect collisionEffect;
    private CollisionPassiveBehaviour collisionPassiveBehaviour;
    
    private DoubleProperty translateX = new SimpleDoubleProperty(100);
    private DoubleProperty translateY = new SimpleDoubleProperty(100);
    private Rotate rotation;
    private double oldX, oldY;
    private double oldDirection;
    private DoubleProperty hp = new SimpleDoubleProperty(100);
    private MoveBehaviour moveBehaviour;
    private Environment environment;
    
    public GameObject(Environment environment, double x, double y, boolean isSettingDisplay){
        translateX.set(x);
        translateY.set(y);
        rotation = new Rotate(0);
        
        this.environment = environment;
        
        if (isSettingDisplay) setDisplay();
    }
    
    /**
     * create a new GameObject inside a created Obj
     * @param owner 
     * @param x the varied X coordinate compared to the owner (child's X = owner's X + x)
     * @param y the varied Y coordinate compared to the owner (child's Y = owner's Y + y)
     */
    public GameObject(GameObject owner, double x, double y, boolean isSettingDisplay){
        owner.child = this;
        this.owner = owner;
        
        translateX.bind(Bindings.add(x, owner.translateX));
        translateY.bind(Bindings.add(y, owner.translateY));
        
        rotation = new Rotate(0 , -x , -y);
        rotation.angleProperty().bind(owner.rotation.angleProperty());
        environment = owner.getEnvironment();
        
        if (isSettingDisplay) setDisplay();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public double getTranslateX() {
        return translateX.get();
    }

    public double getTranslateY() {
        return translateY.get();
    }
    
    public double getOwnerTranslateX() {
        if (owner != null) {
            return owner.getOwnerTranslateX();
        }
        else {
            return getTranslateX();
        }
    }

    public double getOwnerTranslateY() {
        if (owner != null) {
            return owner.getOwnerTranslateY();
        }
        else {
            return getTranslateY();
        }
    }
    
    public double[] getRealCoordinate(){
        double rotateDirection = Math.toRadians(getRotateAngle());
        double xFromPivot = -getRotate().getPivotX();
        double yFromPivot = -getRotate().getPivotY();
        double realX = getOwnerTranslateX() + yFromPivot * Math.cos(rotateDirection) - xFromPivot * Math.sin(rotateDirection);
        double realY = getOwnerTranslateY() + yFromPivot * Math.sin(rotateDirection) + xFromPivot * Math.cos(rotateDirection);
        return new double[] {realX, realY};
    }

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }
    
    public void setOwnerOldX(double oldX) {
        if (owner != null){
            owner.setOwnerOldX(oldX);
        }
        setOldX(oldX);
    }
    
    public void setOwnerOldY(double oldY) {
        if (owner != null){
            owner.setOwnerOldY(oldY);
        }
        setOldY(oldY);
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }
    
    public double getOwnerOldX(){
        if (owner != null){
            return owner.getOwnerOldX();
        }
        return oldX;
    }
    
    public double getOwnerOldY(){
        if (owner != null){
            return owner.getOwnerOldY();
        }
        return oldY;
    }
    
    public Shape getHitBox(){
        return hitBox;
    }
    
    public ImageView getImage() {
        return imageView;
    }

    public Group getDisplay() {
        return display;
    }
    
    public void setRotate(double direction){
        double angle = Math.toDegrees(direction);
        if (owner != null) {
            owner.rotation.setAngle(angle);
        }
        else {
            rotation.setAngle(angle);
        }
    }
    
    public double getRotateAngle(){
        return rotation.getAngle();
    }
    
    public Rotate getRotate(){
        return rotation;
    }
    
    public double getOldDirection(){
        if (owner != null){
            return owner.getOldDirection();
        }
        return oldDirection;
    }
    
    public void setOldDirection(double oldDirection) {
        if (owner != null){
            owner.setOldDirection(oldDirection);
        }
        this.oldDirection = oldDirection;
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

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }

    public void setHp(double hp) {
        this.hp.set(hp);
    }

    public void setTranslateX(double newX) {
        if (owner != null) {
            owner.setTranslateX(newX);
        }
        else {
            translateX.set(newX);
        }
    }

    public void setTranslateY(double newY) {
        if (owner != null) {
            owner.setTranslateY(newY);
        }
        else {
            translateY.set(newY);
        }
    }

    public void setMoveBehaviour(MoveBehaviour moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }

    final public void setCollisionEffect(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    final public void setCollisionPassiveBehaviour(CollisionPassiveBehaviour collisionPassiveBehaviour) {
        this.collisionPassiveBehaviour = collisionPassiveBehaviour;
    }

    public GameObject getOwner() {
        return owner;
    }

    public GameObject getChild() {
        return child;
    }
    
    /**
     * check whether this obj has the specific owner
     * @param owner
     * @return 
     */
    public boolean hasOwner(GameObject owner){
        if (this.owner == null) return false;
        if (this.owner == owner) return true;
        return this.owner.hasOwner(owner);
    }
    
    /**
     * check whether this obj has the specific child
     * @param child
     * @return 
     */
    public boolean hasChild(GameObject child){
        if (this.child == null) return false;
        if (this.child == child) return true;
        return this.child.hasChild(child);
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and return boolean
     * @param target
     * @return 
     */
    public Shape getCollisionShapeWith(GameObject target){
       Shape intersect = Shape.intersect(hitBox, target.getHitBox());
       return intersect;
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and act accordingly
     * @param target 
     */
    public boolean checkCollisionWith(GameObject target){
        Shape collisionShape = getCollisionShapeWith(target);
        if (collisionShape.getBoundsInLocal().getWidth() != -1){
//            System.out.println("collide");
            // let the collision effects affect the two collided objects
            onCollision(target, collisionShape);
            target.onCollision(this, collisionShape);
            return true;
        }
        return false;
    }
    
    /**
     * the action the obj makes upon collision
     * @param target
     * @param collisionShape 
     */
    public void onCollision(GameObject target, Shape collisionShape){
        collisionEffect.affect(this, target, collisionShape);
    }
    
    /**
     * put all the display component inside the display HBox
     */
    public void setDisplay(){
        setDisplayComponents();
        display = new Group(hitBox, imageView);
//        display.setPrefSize(0, 0);
        display.translateXProperty().bind(translateX);
        display.translateYProperty().bind(translateY);
        display.getTransforms().add(rotation);
//        for (Node child : display.getChildren()){
//            child.translateXProperty().bind(translateX);
//            child.translateYProperty().bind(translateY);
//            child.getTransforms().add(rotation);
//        }
        environment.registerGameObj(this, true);
    }
    
    /**
     * update the rectangle shape representing the bounds of the game obj
     */
    public void updateBoundsRectangle(){
        Bounds bounds = display.getBoundsInParent();
        boundsRectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }
    
    public Rectangle getBoundsRectangle(){
        return boundsRectangle;
    }
    
    /**
     * the obj must implement this method to set its hit box and image
     */
    public abstract void setDisplayComponents();
    
    /**
     * the obj must implement this method to update with the environment's animation timer
     * @param now 
     */
    public abstract void update(long now);
}
