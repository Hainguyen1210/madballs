/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

/**
 * do not use the variable direction of MoveBehaviour class, use the obj's Rotate angle instead
 * @author Caval
 */
public class RotateBehaviour extends MoveBehaviour{
    private double targetX, targetY;

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

    public RotateBehaviour(GameObject obj, double speed) {
        super(obj, speed);
    }

    @Override
    void calculateNewCordinate(long now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(long now) {
//        System.out.println(getObject().getClass());
        if (getLastMoveTime() == 0) setLastMoveTime(MadBalls.getGameEnvironment().getLastUpdateTime());
        if ((now - getLastMoveTime()) / 1_000_000_000.0 > 0.001){
            double[] realCoordinate = getObject().getRealCoordinate();
            double newDirection = Math.atan2(getTargetY() - realCoordinate[1], getTargetX() - realCoordinate[0]);
            double currentRotateDirection = Math.toRadians(getObject().getRotateAngle());
            if (newDirection != currentRotateDirection) {
//                System.out.println("");
//                System.out.println(getDirection());
//                System.out.println(newDirection);
                getObject().setOldDirection(currentRotateDirection);
                getObject().setRotate(newDirection);
                setLastMoveTime(now);

//                    MadBalls.getMultiplayerHandler().sendData(new MoveData(getObject().getIndex(), now, currentRotateDirection, newDirection));

            }
        }
    }
    
}
