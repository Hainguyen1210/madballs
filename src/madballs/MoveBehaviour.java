/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 *
 * @author Caval
 */
public abstract class MoveBehaviour {
    private DoubleProperty velocityX = new SimpleDoubleProperty();
    private DoubleProperty velocityY = new SimpleDoubleProperty();
    private LongProperty lastMoveTime = new SimpleLongProperty(0);
    private double speed;
    private double direction = -1;
    private double targetX = -1;
    private double targetY = -1;
    private double newX, newY;
    private GameObject obj;
    
    final MultiplePressedKeysEventHandler keyHandler = 
        new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {
            
            public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
//                try {
//                    if (!((ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) && (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)))) {
////                        velocityX.set(0);
////                        MadBalls.out.writeObject("x 0");
//                    }
//                    if (!((ke.isPressed(KeyCode.UP)  || ke.isPressed(KeyCode.W)) && (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)))) {
////                        velocityY.set(0);
////                        MadBalls.out.writeObject("y 0");
//                    }
//
//
//                    if (ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) {
////                        velocityX.set(-speed);
////                        MadBalls.out.writeObject("x -");
//                    }
//                    if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)) {
////                        velocityX.set(speed);
////                        MadBalls.out.writeObject("x +");
//                    }
//
//                    if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.W)) {
////                        velocityY.set(-speed);
////                        MadBalls.out.writeObject("y -");
//                    }
//                    if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)) {
////                        velocityY.set(speed);
////                        MadBalls.out.writeObject("y +");
//                    }
//                }
//                catch (IOException ex){
//                    
//                }
            }
        });
    
    public MoveBehaviour(GameObject obj, double speed){
        this.obj = obj;
    }
    
    public GameObject getObject(){
        return obj;
    }

    public double getVelocityX() {
        return velocityX.get();
    }

    public double getVelocityY() {
        return velocityY.get();
    }

    public long getLastUpdateTime() {
        return lastMoveTime.get();
    }

    public void setLastUpdateTime(long lastUpdateTime) {
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
    
    /**
     * method for the move behavior to calculate the result coordinate of the obj after the move
     * @param now current timestamp
     */
    abstract void calculateNewCordinate(long now);
    
    public void move(long now){
        calculateNewCordinate(now);
        obj.setTranslateX(newX);
        obj.setTranslateY(newY);
    }
}
