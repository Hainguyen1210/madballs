/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.SpeedBuff;


/**
 *
 * @author haing
 */
public class SpeedBoost extends BuffItem{
    public SpeedBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay, new SpeedBuff(null, 5, 50));
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("yellow"));
        super.setDisplayComponents();
    }
}
