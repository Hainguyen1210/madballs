/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.BuffState;
import madballs.effectState.DamageBuff;
import madballs.effectState.FireRateBuff;
import madballs.effectState.InstantHeal;
import madballs.effectState.SpeedBuff;

/**
 *
 * @author caval
 */
public class DivineBoost extends BuffItem{
    
    public DivineBoost(Environment environment, double x, double y, BuffState effectState) {
        super(environment, x, y, new SpeedBuff(new FireRateBuff(new DamageBuff(new InstantHeal(100), 5, 2), 5, 2), 5, 50));
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("purple"));
        super.setDisplayComponents();
    }
    
}
