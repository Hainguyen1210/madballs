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
public class Rejuvenation extends BuffState{
    private double amount;

    public Rejuvenation(BuffState effectState, int duration, double amount) {
        super(effectState, duration);
        this.amount = amount;
    }

    @Override
    public void apply() {
    }

    @Override
    public void fade() {
        
    }

    @Override
    public void uniqueUpdate(long timestamp) {
        getBall().setHpValue(getBall().getHpValue() + amount/getDuration());
    }
    
}
