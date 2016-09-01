package madballs.item;

import javafx.scene.paint.Color;
import madballs.Environment;
import madballs.buffState.Armor;
import madballs.buffState.Speed;
import madballs.map.SpawnLocation;

/**
 * Created by caval on 01/09/2016.
 */
public class HeavyPlate extends BuffItem {
    public HeavyPlate(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, new Speed(new Armor(null, 8, 100), 8, -5*2.5), id);
    }

    @Override
    public void setDisplayComponents(){
        setColor(Color.BLACK);
        super.setDisplayComponents();
    }
}
