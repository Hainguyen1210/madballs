/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.scene.shape.Rectangle;
import madballs.collision.InvulnerableBehaviour;
import madballs.collision.PushBackEffect;

/**
 * invisible PushBack object that can only be collided forcefully
 * E.g. a projectile is forced to collide with the Ground when it has moved more than its range
 * @author Caval
 */
public class Ground extends GameObject{

    public Ground(Environment environment, double x, double y, Integer id) {
        super(environment, x, y, false, id);
        setCollisionEffect(new PushBackEffect(0, null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }

    @Override
    public void setDisplayComponents() {
        setHitBox(new Rectangle());
    }

    @Override
    public void updateUnique(long now) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
