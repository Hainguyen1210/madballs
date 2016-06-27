/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

/**
 * a collision effect that deals damage to the target
 * @author Caval
 */
public class DamageCollision implements CollisionEffect{
    

    @Override
    public void affect(GameObject target, double amount) {
        target.getCollisionPassiveBehaviour().getDamaged(target, amount);
    }
    
}
