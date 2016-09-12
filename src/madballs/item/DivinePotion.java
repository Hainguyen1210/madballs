/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Environment;
import madballs.buffState.Speed;
import madballs.buffState.Power;
import madballs.buffState.Frenzy;
import madballs.buffState.InstantHeal;
import madballs.map.SpawnLocation;

/**
 * special item give all buff effects
 * @author caval
 */
public class DivinePotion extends BuffItem{

    public DivinePotion(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new Speed(new Frenzy(new Power(new InstantHeal(null, 100), 5, 2), 5, 2), 5, 50), id);
//        System.out.println(((GiveBuffEffect)getCollisionEffect()).getBuffState().getWrappedBuffState() == null);
    }
    
    @Override
    public void setDisplayComponents(){
        setColor(Paint.valueOf("purple"));
        super.setDisplayComponents();
        setImage("purple_potion");
    }
    
}
