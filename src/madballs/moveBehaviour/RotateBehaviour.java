/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.moveBehaviour;

import madballs.GameObject;

/**
 * a GameObject with this MoveBehaviour is able to actively rotate
 * @author Caval
 */
public class RotateBehaviour extends MoveBehaviour{
//    private double targetX, targetY;
    private double newDirection;

    public double getNewDirection() {
        return newDirection;
    }

    public void setNewDirection(double newDirection) {
        this.newDirection = newDirection;
    }

    public RotateBehaviour(GameObject obj, double speed) {
        super(obj, speed);
    }

    @Override
    void calculateNewCoordinate(long now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveUnique(long now) {
//        System.out.println(getObject().getClass());
        if (getLastMoveTime() == 0) setLastMoveTime(getObject().getEnvironment().getLastUpdateTime());
        if ((now - getLastMoveTime()) / 1_000_000_000.0 > 0.001){
//            double[] realCoordinate = getObject().getRealCoordinate();
//            double directionFromTargetToSelf = Math.atan2(getTargetY() - realCoordinate[1], getTargetX() - realCoordinate[0]);
//
//            double distanceFromTargetToOwner = Math.sqrt(Math.pow(targetX - getObject().getOwnerTranslateX(), 2) + Math.pow(targetY - getObject().getOwnerTranslateY(), 2));
//            double directionFromTargetToOwner = directionFromTargetToSelf - Math.atan2(getObject().getDistanceToOwner(), distanceFromTargetToOwner);
//            double scale = SceneManager.getInstance().getScale();
//            double sceneWidth = MadBalls.getAnimationScene().getWidth();
//            double sceneHeight = MadBalls.getAnimationScene().getHeight();
//            double yDiff = getTargetY() - sceneHeight/2 - getObject().getOwnerDiffY()*scale;
//            double xDiff = getTargetX() - sceneWidth/2 - getObject().getOwnerDiffX()*scale;
//            double zoomOut = SceneManager.getInstance().getZoomOut();
//            double mapWidth = getObject().getEnvironment().getMap().getWidth();
//            double mapHeight = getObject().getEnvironment().getMap().getHeight();
//            double yDiff = getTargetY() - getObject().getTranslateY();
//            double xDiff = getTargetX() - getObject().getTranslateX();
//            double newDirection = Math.atan2(yDiff, xDiff);
            double currentRotateDirection = Math.toRadians(getObject().getRotateAngle());
            if (newDirection != currentRotateDirection) {
                getObject().setOldDirection(currentRotateDirection);
                getObject().setRotate(newDirection);
                setLastMoveTime(now);
            }
        }
    }
    
}
