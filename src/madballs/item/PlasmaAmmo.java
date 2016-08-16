/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.PowerUp;

public class PlasmaAmmo extends BuffItem{
    public PlasmaAmmo(Environment environment, double x, double y) {
        super(environment, x, y, new PowerUp(null, 5, 2));
        giveStateEffect.setSoundFX("plasma");
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("red"));
        super.setDisplayComponents();
    }
}
