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
import madballs.map.SpawnLocation;
import madballs.multiplayer.SpawnData;
import madballs.wearables.*;

/**
 * @author haing
 */
public class Spawner {
    private final int MAX_ITEMS = 10;
    private static Class<Weapon>[] weapons = new Class[]{
            Pistol.class, Awp.class, Uzi.class, Ak47.class, Minigun.class,
            M4A1.class, Bazooka.class, XM1104.class, GrenadeLauncher.class, Shield.class, TrapLauncher.class};
    private static Class<Item>[] boostItems = new Class[] {MiniHealthFlask.class, DivinePotion.class, FullPotion.class, SpicyBiscuit.class, PlasmaAmmo.class, Wheels.class};
//    private static Class<Item>[] boostItems = new Class[] {HeavyPlate.class};
    private Random random = new Random();
    private Environment environment;
    private LongProperty lastItemSpawnTime = new SimpleLongProperty(0);
    private SpawnLocation currentSpawnLocation;

    public static Class<Weapon>[] getWeapons() {
        return weapons;
    }

    public static Class<Item>[] getBoostItems() {
        return boostItems;
    }

    public Spawner(Environment environment) {
        this.environment = environment;
//        boostItems = new Class[]{DivinePotion.class};

    }

    public void spawn(long now) {
        if ((now - lastItemSpawnTime.get()) / 1000000000.0 > 5) {
            lastItemSpawnTime.set(now);
            randomSpawn();
        }
    }

    public void randomSpawn() {
//        System.out.print("randomSpawn");
        ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();
//    Map map = environment.getMap();
//    int X = random.nextInt((int) map.getLENGTH());
//    int Y = random.nextInt((int) map.getHEIGHT());
        do {
//            if (isAllSpawned()) return;
            if (getSpawnedQuantity() >= MAX_ITEMS) return;
            currentSpawnLocation = itemSpawnLocations.get(random.nextInt(itemSpawnLocations.size()));
        } while (currentSpawnLocation.isSpawned());


        currentSpawnLocation.setSpawned(true);
        int itemOrWeapon = random.nextInt(2);
        if (itemOrWeapon == 0) {
//            System.out.println("Weapon spawned");
            spawnWeapon(currentSpawnLocation, -1, -1);
        } else {
//            System.out.println("Item spawned");
            spawnItem(currentSpawnLocation, -1, -1);
        }
    }

    private int getSpawnedQuantity(){
        ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();
        int spawnedQuantity = 0;
        for (SpawnLocation spawnLocation : itemSpawnLocations) {
            if (spawnLocation.isSpawned()) {
                spawnedQuantity++;
            }
        }
        return spawnedQuantity;
    }

    private boolean isAllSpawned() {
        ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();
        for (SpawnLocation spawnLocation : itemSpawnLocations) {
            if (!spawnLocation.isSpawned()) {
                return false;
            }
        }
        return true;
    }

    public void spawnWeapon(SpawnLocation spawnLocation, int weaponIndex, Integer id) {
        double X = spawnLocation.getX();
        double Y = spawnLocation.getY();
        if (weaponIndex < 0) {
            weaponIndex = random.nextInt(weapons.length);
        }
        spawnLocation.setTypeNumber(weaponIndex);
        spawnLocation.setType("weapon");
        Class<Weapon> weaponClass = weapons[weaponIndex];
        WeaponItem newItem = new WeaponItem(environment, weaponClass, spawnLocation, id);
        if (MadBalls.isHost()) {
            MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false, newItem.getID()));
        }


//        System.out.print(weaponClass);
//        System.out.println(newItem.getID());
    }

    public void spawnItem(SpawnLocation spawnLocation, int itemIndex, Integer id) {
        double X = spawnLocation.getX();
        double Y = spawnLocation.getY();
        if (itemIndex < 0) {
            itemIndex = random.nextInt(boostItems.length);
        }
        spawnLocation.setTypeNumber(itemIndex);
        spawnLocation.setType("item");
        Class<Item> itemClass = boostItems[itemIndex];
        try {
            Item newItem = itemClass.getDeclaredConstructor(Environment.class, SpawnLocation.class, Integer.class).newInstance(environment, spawnLocation, id);
//            System.out.print(itemClass);
//            System.out.println(newItem.getID());
            if (MadBalls.isHost()) {
                MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false, newItem.getID()));
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Spawner.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
