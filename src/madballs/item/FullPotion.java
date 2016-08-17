/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.InstantHeal;

/**
 *
 * @author chim-
 */
public class FullPotion extends BuffItem{
    public FullPotion(Environment environment, double x, double y) {
        super(environment, x, y, new InstantHeal(null, 100));
    }
    
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("green"));
        super.setDisplayComponents();
    }
}
