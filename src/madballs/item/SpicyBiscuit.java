/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.Frenzy;

/**
 *
 * @author chim-
 */
public class SpicyBiscuit extends BuffItem{

    public SpicyBiscuit(Environment environment, double x, double y) {
        super(environment, x, y, new Frenzy(null, 5, 1.5));
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("blue"));
        super.setDisplayComponents();
    }
}
