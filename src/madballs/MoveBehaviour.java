/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Caval
 */
public abstract class MoveBehaviour {
    private DoubleProperty velocityX = new SimpleDoubleProperty();
    private DoubleProperty velocityY = new SimpleDoubleProperty();
    final double speed = 100;
    private double newX, newY;
    private GameObject obj;
    
    final MultiplePressedKeysEventHandler keyHandler = 
        new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {
            
            public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                try {
                    if (!((ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) && (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)))) {
                        velocityX.set(0);
                        MadBalls.out.writeObject("x 0");
                    }
                    if (!((ke.isPressed(KeyCode.UP)  || ke.isPressed(KeyCode.W)) && (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)))) {
                        velocityY.set(0);
                        MadBalls.out.writeObject("y 0");
                    }


                    if (ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) {
                        velocityX.set(-speed);
                        MadBalls.out.writeObject("x -");
                    }
                    if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)) {
                        velocityX.set(speed);
                        MadBalls.out.writeObject("x +");
                    }

                    if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.W)) {
                        velocityY.set(-speed);
                        MadBalls.out.writeObject("y -");
                    }
                    if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)) {
                        velocityY.set(speed);
                        MadBalls.out.writeObject("y +");
                    }
                }
                catch (IOException ex){
                    
                }
            }
        });
    
    public MoveBehaviour(GameObject obj){
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
    
    abstract void calculateNewCordinate(long now);
    
    public void move(long now){
        calculateNewCordinate(now);
        obj.setTranslateX(newX);
        obj.setTranslateY(newY);
    }
}
