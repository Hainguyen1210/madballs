/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.InstantHeal;
import madballs.map.SpawnLocation;

/**
 * Item refill full HP
 * @author chim-
 */
public class FullPotion extends BuffItem{

    public FullPotion(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new InstantHeal(null, 100), id);
    }
    
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("green"));
        super.setDisplayComponents();
        setImage("red_potion");
    }
}
