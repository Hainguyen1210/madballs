/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.Speed;
import madballs.map.SpawnLocation;


/**
 *
 * @author haing
 */
public class Lightning extends BuffItem{
    public Lightning(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new Speed(null, 5, 50), id);
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("yellow"));
        super.setDisplayComponents();
        setImage("lightning");
    }
}
