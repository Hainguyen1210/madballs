/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.projectiles.Projectile;
import madballs.*;
import madballs.collision.CollisionEffect;
import madballs.collision.CollisionPassiveBehaviour;
import madballs.multiplayer.FireData;

/**
 *
 * @author Caval
 */
public abstract class Weapon extends GameObject{
    private String projectileImageName;
    private double projectileHitBoxSize;
    private Paint projectileColor;
    
    private CollisionEffect projectileCollisionEffect;
    private CollisionPassiveBehaviour projectileCollisionBehaviour;
    private LongProperty lastShotTime = new SimpleLongProperty(0);
    private double height, width;
    private double damage = -1, fireRate = -1, range = -1, ammo = -1, projectileSpeed = -1;
    
    public double getDamage() {
        return damage;
    }

    final public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Paint getProjectileColor() {
        return projectileColor;
    }

    public void setProjectileColor(Paint projectileColor) {
        this.projectileColor = projectileColor;
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

    final public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    public double getRange() {
        return range;
    }

    final public void setRange(double range) {
        this.range = range;
    }

    public double getAmmo() {
        return ammo;
    }

    final public void setAmmo(double ammo) {
        this.ammo = ammo;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    final public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public Weapon(GameObject owner, double x, double y) {
        super(owner, x, y, true);
        setMoveBehaviour(new RotateBehaviour(this, -1));
    }
//    public Weapon(Environment environment, double x, double y) {
//        super(environment, x, y, true);
//        setMoveBehaviour(new RotateBehaviour(this, -1));
//    }

    public CollisionEffect getProjectileCollisionEffect() {
        return projectileCollisionEffect;
    }

    final public void setProjectileCollisionEffect(CollisionEffect projectileCollisionEffect) {
        this.projectileCollisionEffect = projectileCollisionEffect;
    }

    public CollisionPassiveBehaviour getProjectileCollisionBehaviour() {
        return projectileCollisionBehaviour;
    }

    final public void setProjectileCollisionBehaviour(CollisionPassiveBehaviour projectileCollisionBehaviour) {
        this.projectileCollisionBehaviour = projectileCollisionBehaviour;
    }

    public String getProjectileImageName() {
        return projectileImageName;
    }

    public void setProjectileImageName(String projectileImageName) {
        this.projectileImageName = projectileImageName;
    }

    public double getProjectileHitBoxSize() {
        return projectileHitBoxSize;
    }

    final public void setProjectileHitBoxSize(double size) {
        this.projectileHitBoxSize = size;
    }
    
    public void forceFire(){
        new Projectile(this, new Circle(projectileHitBoxSize, projectileColor), projectileImageName);
    }
    
    public void attack(long now){
        if ((now - getLastShotTime()) / 1_000_000_000.0  >  1  / fireRate){
            if (MadBalls.isHost()){
                if (getLastShotTime() == 0) setLastShotTime(getEnvironment().getLastUpdateTime());
                MadBalls.getMultiplayerHandler().sendData(new FireData(getIndex()));
                new Projectile(this, new Circle(projectileHitBoxSize, projectileColor), projectileImageName);
                setLastShotTime(now);
            }
        }
        
    };
    
    @Override
    public void updateUnique(long now) {
//        System.out.println(owner.getTranslateY());
//        System.out.println(getTranslateY());
        if (getMoveBehaviour().isMousePressed()) attack(now);
        getMoveBehaviour().move(now);
    }
}
