/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Ball;
import madballs.Environment;
import madballs.GameObject;
import madballs.Obstacle;
import madballs.collision.*;
import madballs.map.SpawnLocation;
import madballs.wearables.Weapon;


/**
 * @author haing
 */
public class WeaponItem extends Item {
    private Weapon weapon;

    public Weapon getWeapon() {
        return weapon;
    }

    public WeaponItem(Environment environment, Class<Weapon> weaponClass, SpawnLocation spawnLocation, Integer id) {
        super(environment, spawnLocation, id);
        try {
            weapon = weaponClass.getDeclaredConstructor(GameObject.class, Integer.class).newInstance(this, -1);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(WeaponItem.class.getName()).log(Level.SEVERE, null, ex);
        }

        weapon.setMobile(false);
        weapon.setCollisionPassiveBehaviour(new ObjExclusiveBehaviour(new DisappearWithOwnerBehaviour(new ReleaseSpawnLocation(null, getSpawnLocation())), new Class[]{Ball.class, Obstacle.class}));
        weapon.setCollisionEffect(new GiveWeaponEffect(null, weapon));
        setCollisionEffect(new NullEffect(null));
        setCollisionPassiveBehaviour(new InvulnerableBehaviour(null));
    }


    @Override
    public void setDisplayComponents() {
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setColor(Paint.valueOf("blue"));
        setSize(1);
        setHitBox(new Circle(getSize(), getColor()));
    }

}
