/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.effectState;

/**
 *
 * @author caval
 */
public class InstantHeal extends InstantBuff{
    private double amount;

    public InstantHeal(double amount) {
        this.amount = amount;
    }

    @Override
    public void apply() {
        getBall().setHpValue(amount);
    }
    
}
