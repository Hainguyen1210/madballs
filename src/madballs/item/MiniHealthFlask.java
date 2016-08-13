/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Color;
import madballs.Environment;
import madballs.effectState.Rejuvenation;

/**
 *
 * @author caval
 */
public class MiniHealthFlask extends BuffItem{
    
    public MiniHealthFlask(Environment environment, double x, double y) {
        super(environment, x, y, new Rejuvenation(null, 5, 30));
    }
    
    
    
    @Override
    public void setDisplayComponents(){
        setColor(Color.LIGHTGREEN);
        super.setDisplayComponents();
    }
    
}
