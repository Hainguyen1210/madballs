/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.Haste;
import madballs.map.SpawnLocation;


/**
 *
 * @author haing
 */
public class Wheels extends BuffItem{
    public Wheels(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new Haste(null, 5, 50), id);
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("yellow"));
        super.setDisplayComponents();
    }
}
