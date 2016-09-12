/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.moveBehaviour;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import madballs.GameObject;
import madballs.gameFX.SoundStudio;
import madballs.projectiles.Projectile;

/**
 * the GameObject with this MoveBehaviour can actively move in a straight line
 * @author Caval
 */
public class StraightMove extends MoveBehaviour{
    private final DoubleProperty velocityX = new SimpleDoubleProperty();
    private final DoubleProperty velocityY = new SimpleDoubleProperty();
    private double newX, newY;
    private double direction;
    
    public double getNewDirection() {
        return direction;
    }

    public void setNewDirection(double direction) {
        this.direction = direction;
        setVelocityX(Math.cos(direction) * getSpeed());
        setVelocityY(Math.sin(direction) * getSpeed());
//        velocityX.bind(Bindings.multiply(speedProperty(), Math.cos(direction)));
//        velocityY.bind(Bindings.multiply(speedProperty(), Math.sin(direction)));
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

    public double getNewX() {
        return newX;
    }

    public double getNewY() {
        return newY;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    public void setVelocityY(double velocityY) {
        this.velocityY.set(velocityY);
    }
    
    public StraightMove(GameObject obj, double speed) {
        super(obj, speed);
        speedProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double ratio = 1 / (double)oldValue * (double)newValue;
                if (getVelocityX() != 0) setVelocityX(getVelocityX() * ratio);
                if (getVelocityY() != 0) setVelocityY(getVelocityY() * ratio);
            }
        });
    }

    @Override
    void calculateNewCoordinate(long now) {
        // get the time elapsed and update lastUpdateTime
        if (getLastMoveTime() == 0) setLastMoveTime(now);
        double elapsedSeconds = (now - getLastMoveTime()) / 1_000_000_000.0 ;
//        if (now - getLastMoveTime() < 5000000) return;

        setLastMoveTime(now);
        
        // calculate new coordinates
        final double oldX = getObject().getTranslateX();
        final double deltaX = elapsedSeconds * getVelocityX();
        final double newX = oldX + deltaX;
        if (getObject().getTranslateX() != newX) {
            getObject().setOldX(oldX);
        }
        setNewX(newX);
        
        final double oldY = getObject().getTranslateY();
        final double deltaY = elapsedSeconds * getVelocityY();
        final double newY = oldY + deltaY;
        if (getObject().getTranslateY() != newY) {
            getObject().setOldY(oldY);
        }
        setNewY(newY);

        double newDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        setMovedDistance(getMovedDistance() + newDistance);

//        MadBalls.getMultiplayerHandler().sendData(new MoveData(getObject().getIndex(), now, oldX, newX, oldY, newY, getMovedDistance()));
        
        // set the coordinate to the target if the obj has passed the target
//        if (getTargetX() != -1 && newX >= getTargetX()){
//            setNewX(getTargetX());
//            setNewY(getTargetY());
//        }
    }
    
    
    @Override
    public void moveUnique(long now){
        GameObject obj = getObject();
        if (getVelocityX() != 0 || getVelocityY() != 0){
            if (getSoundFX() != null){
                SoundStudio.getInstance().playAudio(getSoundFX(), 0.2, this.toString(), now,
                        obj.getTranslateX(), obj.getTranslateY(), 100, 100);
            }
        }
        calculateNewCoordinate(now);

//        if (obj.getTranslateX() != getNewX()) obj.setOldX(obj.getTranslateX());
//        if (obj.getTranslateY() != getNewY()) obj.setOldY(obj.getTranslateY());
        obj.setTranslateX(getNewX());
        obj.setTranslateY(getNewY());
    }
    
}
