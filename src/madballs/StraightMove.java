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

    public StraightMove(GameObject obj) {
        super(obj);
    }

    @Override
    void calculateNewCordinate(long now) {
        double maxX = 550;
        final double elapsedSeconds = 0.05;
        final double deltaX = elapsedSeconds * getVelocityX();
        final double oldX = getObject().getTranslateX();
        final double newX = Math.max(25, Math.min(maxX, oldX + deltaX));
        setNewX(newX);

        double maxY = 450;
        final double deltaY = elapsedSeconds * getVelocityY();
        final double oldY = getObject().getTranslateY();
        final double newY = Math.max(25, Math.min(maxY, oldY + deltaY));
        setNewY(newY);
    }
    
}
