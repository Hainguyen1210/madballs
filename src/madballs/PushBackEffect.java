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
public class PushBackEffect implements CollisionEffect{

    @Override
    public void affect(GameObject target, double amount) {
        target.sufferPushBack();
    }
    
}
