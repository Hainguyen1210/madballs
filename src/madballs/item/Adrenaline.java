/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.Frenzy;
import madballs.map.SpawnLocation;

/**
 * give Fenzy effect (increase fire rate)
 * @author chim-
 */
public class Adrenaline extends BuffItem{

    public Adrenaline(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new Frenzy(null, 5, 1.5), id);
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("blue"));
        super.setDisplayComponents();
        setImage("blue_syringe");
    }
}
