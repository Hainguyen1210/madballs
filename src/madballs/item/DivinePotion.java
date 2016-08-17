/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.BuffState;
import madballs.effectState.PowerUp;
import madballs.effectState.Frenzy;
import madballs.effectState.InstantHeal;
import madballs.effectState.Haste;
import madballs.map.SpawnLocation;

/**
 *
 * @author caval
 */
public class DivinePotion extends BuffItem{

    public DivinePotion(Environment environment, SpawnLocation spawnLocation) {
        super(environment, spawnLocation, new Haste(new Frenzy(new PowerUp(new InstantHeal(null, 100), 5, 2), 5, 2), 5, 50));
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("purple"));
        super.setDisplayComponents();
    }
    
}
