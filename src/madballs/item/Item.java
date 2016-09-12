/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import javafx.scene.paint.Paint;
import madballs.Ball;
import madballs.Obstacle;
import madballs.collision.ObjExclusiveBehaviour;
import madballs.collision.DisappearBehaviour;
import madballs.Environment;
import madballs.GameObject;
import madballs.collision.ReleaseSpawnLocation;
import madballs.map.SpawnLocation;

/**
 * Item object
 * @author chim-
 */
public abstract class Item extends GameObject {
    private SpawnLocation spawnLocation;
    private int size;
    private Paint color;

    public int getSize() {
        return size;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(SpawnLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Item(Environment environment, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation.getX(), spawnLocation.getY(), true, id);
        setMobile(false);
        setSpawnLocation(spawnLocation);
//      setDisplay();
//      setCollisionEffect(new NullEffect(null));
        setCollisionPassiveBehaviour(new ObjExclusiveBehaviour(new Class[]{Ball.class, Obstacle.class}, new DisappearBehaviour(new ReleaseSpawnLocation(spawnLocation, null))));
    }

    @Override
    public void updateUnique(long now) {

    }

}