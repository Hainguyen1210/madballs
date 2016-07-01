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
    private double movedDistance = 0;

    public StraightMove(GameObject obj, double speed) {
        super(obj, speed);
    }

    @Override
    void calculateNewCordinate(long now) {
        // get the time elapsed and update lastUpdateTime
        if (getLastMoveTime() == 0) setLastMoveTime(MadBalls.getGameEnvironment().getLastUpdateTime());
//        if (now - getLastMoveTime() < 5000000) return;
        
        
//        System.out.println(now - getLastUpdateTime());
        final double elapsedSeconds = (now - getLastMoveTime()) / 1_000_000_000.0 ;
//        System.out.println(elapsedSeconds);
        setLastMoveTime(now);
        
        // return if the obj has reached its target
        
        // calculate new coordinates
        final double oldX = getObject().getTranslateX();
        final double deltaX = elapsedSeconds * getVelocityX();
        final double newX = oldX + deltaX;
        if (getObject().getTranslateX() != newX) {
            getObject().setOldX(oldX - 0.5 * Math.signum(deltaX));
        }
        setNewX(newX);
        
        final double oldY = getObject().getTranslateY();
        final double deltaY = elapsedSeconds * getVelocityY();
        final double newY = oldY + deltaY;
        if (getObject().getTranslateY() != newY) {
            getObject().setOldY(oldY - 0.5 * Math.signum(deltaY));
        }
        setNewY(newY);
        
        movedDistance += getSpeed() * elapsedSeconds;
        
        // set the coordinate to the target if the obj has passed the target
//        if (getTargetX() != -1 && newX >= getTargetX()){
//            setNewX(getTargetX());
//            setNewY(getTargetY());
//        }
    }

    public double getMovedDistance() {
        return movedDistance;
    }
    
    
    @Override
    public void move(long now){
        GameObject obj = getObject();
        calculateNewCordinate(now);
//        if (obj.getTranslateX() != getNewX()) obj.setOldX(obj.getTranslateX());
//        if (obj.getTranslateY() != getNewY()) obj.setOldY(obj.getTranslateY());
        obj.setTranslateX(getNewX());
        obj.setTranslateY(getNewY());
    }
    
}
