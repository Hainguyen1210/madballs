/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.DamageBuff;

public class DamageBoost extends BuffItem{
    public DamageBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay, new DamageBuff(null, 5, 2));
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("red"));
        super.setDisplayComponents();
    }
}
