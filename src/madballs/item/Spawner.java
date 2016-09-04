/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Environment;
import madballs.GameObject;
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
    private static HashMap<Class, Integer> itemSpawnProbability = new HashMap<Class, Integer>()
    {{
        put(Pistol.class, 10);
        put(Awp.class, 4);
        put(Uzi.class, 8);
        put(Ak47.class, 8);
        put(Minigun.class, 4);
        put(M4A1.class, 8);
        put(Bazooka.class, 3);
        put(XM1104.class, 7);
        put(GrenadeLauncher.class, 3);
        put(Shield.class, 8);
        put(TrapLauncher.class, 3);

        put(MiniHealthFlask.class, 8);
        put(DivinePotion.class, 2);
        put(FullPotion.class, 3);
        put(SpicyBiscuit.class, 10);
        put(PlasmaAmmo.class, 8);
        put(Wheels.class, 8);
    }};

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
        // get spawn location list
        ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();

        // keep finding empty spawn location
        do {
//            if (isAllSpawned()) return;
            if (getSpawnedQuantity() >= MAX_ITEMS) return;
            currentSpawnLocation = itemSpawnLocations.get(random.nextInt(itemSpawnLocations.size()));
        } while (currentSpawnLocation.isSpawned());

        // spawn item or weapon on that location
//        currentSpawnLocation.setSpawned(true);
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

//    private boolean isAllSpawned() {
//        ArrayList<SpawnLocation> itemSpawnLocations = environment.getMap().getItemSpawnLocations();
//        for (SpawnLocation spawnLocation : itemSpawnLocations) {
//            if (!spawnLocation.isSpawned()) {
//                return false;
//            }
//        }
//        return true;
//    }

    public void spawnWeapon(SpawnLocation spawnLocation, int weaponIndex, Integer id) {
//        double X = spawnLocation.getX();
//        double Y = spawnLocation.getY();
        if (weaponIndex < 0) {
            weaponIndex = random.nextInt(weapons.length);
        }
        Class<Weapon> weaponClass = weapons[weaponIndex];

        // random probability
        int probability = random.nextInt(10)+1 ;
        if (probability <= itemSpawnProbability.get(weaponClass)){

            spawnLocation.setSpawned(true);
            spawnLocation.setTypeNumber(weaponIndex);
            spawnLocation.setType("weapon");
            WeaponItem newItem = new WeaponItem(environment, weaponClass, spawnLocation, id);
            if (MadBalls.isHost()) {
                MadBalls.getMultiplayerHandler().sendData(new SpawnData(spawnLocation, false, newItem.getID()));
            }
            System.out.println(probability + "/" + itemSpawnProbability.get(weaponClass)+ " " + weaponClass.toString() + "*");
        } else {
            System.out.println(probability + "/" + itemSpawnProbability.get(weaponClass)+ " " + weaponClass.toString());
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
        Class<Item> itemClass = boostItems[itemIndex];
        // random probability
        int probability = random.nextInt(10)+1 ;
        if (probability <= itemSpawnProbability.get(itemClass)) {

            spawnLocation.setSpawned(true);
            spawnLocation.setTypeNumber(itemIndex);
            spawnLocation.setType("item");
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
            System.out.println(probability + "/" + itemSpawnProbability.get(itemClass) + " " + itemClass.toString() + "*");
        } else {
            System.out.println(probability + "/" + itemSpawnProbability.get(itemClass) + " " + itemClass.toString());
        }

    }
}
