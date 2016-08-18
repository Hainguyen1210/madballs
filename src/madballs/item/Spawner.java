/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.map.SpawnLocation;
import madballs.multiplayer.SpawnData;
import madballs.wearables.*;

/**
 * @author haing
 */
public class Spawner {
    private Class<Weapon>[] weapons;
    private Class<Item>[] boostItems;
    private Random random = new Random();
    private Environment environment;
    private LongProperty lastItemSpawnTime = new SimpleLongProperty(0);
    private ArrayList<SpawnLocation> itemSpawnLocations;
    private SpawnLocation currentSpawnLocation;


    public Spawner(Environment environment) {
        this.environment = environment;
        weapons = new Class[]{Awp.class, Uzi.class, Ak47.class, Minigun.class, M4A1.class, Pistol.class, RocketLauncher.class, XM1104.class};
//        boostItems = new Class[]{DivinePotion.class};
        boostItems = new Class[] {MiniHealthFlask.class, DivinePotion.class, FullPotion.class, SpicyBiscuit.class, PlasmaAmmo.class, Wheels.class};
    }

    public void spawn(long now) {
        if ((now - lastItemSpawnTime.get()) / 1000000000.0 > 5) {
            lastItemSpawnTime.set(now);
            randomSpawn();
        }
    }

    public void randomSpawn() {
        System.out.print("randomSpawn");
        itemSpawnLocations = environment.getMap().getItemSpawnLocations();
//    Map map = environment.getMap();
//    int X = random.nextInt((int) map.getLENGTH());
//    int Y = random.nextInt((int) map.getHEIGHT());
        do {
            if (isAllSpawned()) return;
            currentSpawnLocation = itemSpawnLocations.get(random.nextInt(itemSpawnLocations.size()));
        } while (currentSpawnLocation.isSpawned());


        currentSpawnLocation.setSpawned(true);
        int itemOrWeapon = random.nextInt(2);
        if (itemOrWeapon == 0) {
            System.out.println("Weapon spawned");
            spawnWeapon(currentSpawnLocation, -1);
        } else {
            System.out.println("Item spawned");
            spawnItem(currentSpawnLocation, -1);
        }
    }

    private boolean isAllSpawned() {
        for (SpawnLocation spawnLocation : itemSpawnLocations) {
            if (!spawnLocation.isSpawned()) {
                return false;
            }
        }
        return true;
    }

    public void spawnWeapon(SpawnLocation spawnLocation, int weaponIndex) {
        double X = spawnLocation.getX();
        double Y = spawnLocation.getY();
        if (weaponIndex < 0) {
            weaponIndex = random.nextInt(weapons.length);
        }
        spawnLocation.setTypeNumber(weaponIndex);
        spawnLocation.setType("weapon");
        Class<Weapon> weaponClass = weapons[weaponIndex];
        if (MadBalls.isHost()) {
            MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false));
        }
        new WeaponItem(environment, weaponClass, spawnLocation);


        System.out.println(weaponClass);
    }

    public void spawnItem(SpawnLocation spawnLocation, int itemIndex) {
        double X = spawnLocation.getX();
        double Y = spawnLocation.getY();
        if (itemIndex < 0) {
            itemIndex = random.nextInt(boostItems.length);
        }
        spawnLocation.setTypeNumber(itemIndex);
        Class<Item> itemClass = boostItems[itemIndex];
        if (MadBalls.isHost()) {
            MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false));
        }
        try {
            itemClass.getDeclaredConstructor(Environment.class, SpawnLocation.class).newInstance(environment, spawnLocation);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Spawner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
