/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.PowerUp;
import madballs.map.SpawnLocation;

public class PlasmaAmmo extends BuffItem{
    public PlasmaAmmo(Environment environment, SpawnLocation spawnLocation) {
        super(environment, spawnLocation, new PowerUp(null, 5, 2));
        giveBuffEffect.setSoundFX("plasma");
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("red"));
        super.setDisplayComponents();
    }
}
