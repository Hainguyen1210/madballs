/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

/**
 *
 * @author Caval
 */
public class StraightMove extends MoveBehaviour{

    public StraightMove(GameObject obj, double speed) {
        super(obj, speed);
    }

    @Override
    void calculateNewCordinate(long now) {
        // get the time elapsed and update lastUpdateTime
        if (getLastUpdateTime() == 0) setLastUpdateTime(MadBalls.getGameEnvironment().getLastUpdateTime());
        final double elapsedSeconds = (now - getLastUpdateTime()) / 1_000_000_000 ;
        setLastUpdateTime(now);
        
        // return if the obj has reached its target
        final double oldX = getObject().getTranslateX();
        if (getTargetX() != -1 && oldX == getTargetX()){
            return;
        }
        
        // calculate new coordinates
        final double deltaX = elapsedSeconds * getVelocityX();
        final double newX = Math.max(getObject().getMinX(), Math.min(getObject().getMaxX(), oldX + deltaX));
        setNewX(newX);
        
        final double oldY = getObject().getTranslateY();
        final double deltaY = elapsedSeconds * getVelocityY();
        final double newY = Math.max(getObject().getMinY(), Math.min(getObject().getMaxY(), oldY + deltaY));
        setNewY(newY);
        
        
        // set the coordinate to the target if the obj has passed the target
        if (getTargetX() != -1 && newX >= getTargetX()){
            setNewX(getTargetX());
            setNewY(getTargetY());
        }
    }
    
}
