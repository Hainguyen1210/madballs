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
 *
 * @author Caval
 */
public class Ground extends GameObject{

    public Ground(Environment environment, double x, double y) {
        super(environment, x, y, false);
        setCollisionEffect(new PushBackEffect(null , 0));
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
