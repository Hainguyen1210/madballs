/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.moveBehaviour;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.GameObject;
import madballs.gameFX.SoundStudio;

/**
 * the class determines how the movement of a GameObject is made
 * @author Caval
 */
public abstract class MoveBehaviour {
    private final LongProperty lastMoveTime = new SimpleLongProperty(0);
    private final BooleanProperty mousePressed = new SimpleBooleanProperty(false);
    private DoubleProperty speed = new SimpleDoubleProperty();
    private double movedDistance = 0;
    private GameObject obj;
    private String soundFX;
    
    public MoveBehaviour(GameObject obj, double speed){
        this.obj = obj;
        setSpeed(speed);
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

    public long getLastMoveTime() {
        return lastMoveTime.get();
    }

    public void setLastMoveTime(long lastUpdateTime) {
        this.lastMoveTime.set(lastUpdateTime);
    }

    public double getSpeed() {
        return speed.get();
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }

    public double getMovedDistance() {
        return movedDistance;
    }

    public void setMovedDistance(double movedDistance) {
        this.movedDistance = movedDistance;
    }

    public String getSoundFX() {
        return soundFX;
    }

    public void setSoundFX(String soundFX) {
        this.soundFX = soundFX;
    }

    /**
     * method for the move behavior to calculate the result coordinate of the obj after the move
     * @param now current timestamp
     */
    abstract void calculateNewCoordinate(long now);
    
    public abstract void moveUnique(long now);

    public void move(long now){
        moveUnique(now);
    }
}
