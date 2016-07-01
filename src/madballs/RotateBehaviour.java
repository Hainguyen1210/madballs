/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import madballs.Wearables.Weapon;

/**
 *
 * @author Caval
 */
public class RotateBehaviour extends MoveBehaviour{

    public RotateBehaviour(GameObject obj, double speed) {
        super(obj, speed);
    }

    @Override
    void calculateNewCordinate(long now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(long now) {
        if (getLastMoveTime() == 0) setLastMoveTime(MadBalls.getGameEnvironment().getLastUpdateTime());
        if ((now - getLastMoveTime()) / 1_000_000_000.0 > 0.001){
            double[] realCoordinate = getObject().getRealCoordinate();
            double newDirection = Math.atan2(getTargetY() - realCoordinate[1], getTargetX() - realCoordinate[0]);
            if (newDirection != getDirection()) {
//                setOldDirection(getDirection());
                setDirection(newDirection);
                getObject().setRotate(getDirection());
                setLastMoveTime(now);
            }
        }
    }
    
}
