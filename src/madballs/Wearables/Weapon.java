/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Wearables;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import madballs.Projectiles.Projectile;
import madballs.*;
import madballs.Collision.CollisionEffect;
import madballs.Collision.CollisionPassiveBehaviour;

/**
 *
 * @author Caval
 */
public abstract class Weapon extends GameObject{
    private CollisionEffect projectileCollisionEffect;
    private CollisionPassiveBehaviour projectileCollisionBehaviour;
    private LongProperty lastShotTime = new SimpleLongProperty(0);
    private Ball owner;
    private double damage = -1, fireRate = -1, range = -1, ammo = -1, projectileSpeed = -1;

    public Ball getOwner() {
        return owner;
    }

    public void setOwner(Ball owner) {
        this.owner = owner;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
    
    
    public long getLastShotTime() {
        return lastShotTime.get();
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime.set(lastShotTime);
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getAmmo() {
        return ammo;
    }

    public void setAmmo(double ammo) {
        this.ammo = ammo;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public Weapon(Ball owner) {
        super(owner);
        this.owner = owner;
    }

    public CollisionEffect getProjectileCollisionEffect() {
        return projectileCollisionEffect;
    }

    public void setProjectileCollisionEffect(CollisionEffect projectileCollisionEffect) {
        this.projectileCollisionEffect = projectileCollisionEffect;
    }

    public CollisionPassiveBehaviour getProjectileCollisionBehaviour() {
        return projectileCollisionBehaviour;
    }

    public void setProjectileCollisionBehaviour(CollisionPassiveBehaviour projectileCollisionBehaviour) {
        this.projectileCollisionBehaviour = projectileCollisionBehaviour;
    }
    
    public void attack(long now){
        if (getLastShotTime() == 0) setLastShotTime(getEnvironment().getLastUpdateTime());
        
        if (now - getLastShotTime() > 1 / fireRate){
            new Projectile(this);
            setLastShotTime(now);
        }
    };
    
    @Override
    public void update(long now) {
        attack(now);
    }
}
