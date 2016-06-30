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
        double newDirection = Math.atan2(getTargetY() - getObject().getTranslateY(), getTargetX() - getObject().getTranslateX());
        if (newDirection != getDirection()) {
            setOldDirection(getDirection());
            setDirection(newDirection);
        }
        getObject().setRotate(getDirection());
        setNeedUpdate(false);
    }
    
}
