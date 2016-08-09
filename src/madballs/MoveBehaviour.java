/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 *
 * @author Caval
 */
public abstract class MoveBehaviour {
    private final DoubleProperty velocityX = new SimpleDoubleProperty();
    private final DoubleProperty velocityY = new SimpleDoubleProperty();
    private final LongProperty lastMoveTime = new SimpleLongProperty(0);
    private final BooleanProperty mousePressed = new SimpleBooleanProperty(false);
    private double speed;
    private double direction = -1;
    private double targetX = -1;
    private double targetY = -1;
    private double newX, newY;
    private double movedDistance = 0;
    private GameObject obj;
    
    public MoveBehaviour(GameObject obj, double speed){
        this.obj = obj;
        this.speed = speed;
    }
    
    public GameObject getObject(){
        return obj;
    }
    
    public boolean isMousePressed(){
        return mousePressed.get();
    }
    
    public void setMousePressed(boolean isPressed){
        mousePressed.set(isPressed);
    }

    public double getVelocityX() {
        return velocityX.get();
    }

    public double getVelocityY() {
        return velocityY.get();
    }

    public long getLastMoveTime() {
        return lastMoveTime.get();
    }

    public void setLastMoveTime(long lastUpdateTime) {
        this.lastMoveTime.set(lastUpdateTime);
    }

    public void setNewX(double newX) {
        this.newX = newX;
    }

    public void setNewY(double newY) {
        this.newY = newY;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    public void setVelocityY(double velocityY) {
        this.velocityY.set(velocityY);
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
        setVelocityX(Math.cos(direction) * speed);
        setVelocityY(Math.sin(direction) * speed);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getNewX() {
        return newX;
    }

    public double getNewY() {
        return newY;
    }

    public double getMovedDistance() {
        return movedDistance;
    }

    public void setMovedDistance(double movedDistance) {
        this.movedDistance = movedDistance;
    }
    /**
     * method for the move behavior to calculate the result coordinate of the obj after the move
     * @param now current timestamp
     */
    abstract void calculateNewCordinate(long now);
    
    public abstract void move(long now);
}
