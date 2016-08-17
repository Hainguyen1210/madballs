/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.effectState.InstantHeal;
import madballs.map.SpawnLocation;

/**
 *
 * @author chim-
 */
public class FullPotion extends BuffItem{
    public FullPotion(Environment environment, SpawnLocation spawnLocation) {
        super(environment, spawnLocation, new InstantHeal(100));
    }
    
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("green"));
        super.setDisplayComponents();
    }
}
