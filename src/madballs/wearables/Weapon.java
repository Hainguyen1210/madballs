/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.wearables;


import javafx.beans.property.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.collision.*;
import madballs.gameFX.SoundStudio;
import madballs.moveBehaviour.MoveBehaviour;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.multiplayer.FireData;
import madballs.multiplayer.GetWeaponData;
import madballs.projectiles.Projectile;

/**
 *
 * @author Caval
 */
public abstract class Weapon extends GameObject {
    private String projectileImageName;
    private double projectileHitBoxSize;
    private Paint projectileColor;
    private String fireSoundFX;
    private double weight = 0;

    private CollisionEffect projectileCollisionEffect;
    private CollisionPassiveBehaviour projectileCollisionBehaviour;
    private MoveBehaviour projectileMoveBehaviour;
    private double scope = 1;
    private LongProperty lastShotTime = new SimpleLongProperty(0);
    private double height, width;
    private double fireRate = -1, range = -1, projectileSpeed = -1;
    private DoubleProperty damage = new SimpleDoubleProperty();
    private double maxRange = -1;
    private IntegerProperty ammo = new SimpleIntegerProperty(-1);

    public LongProperty lastShotTimeProperty() {
        return lastShotTime;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    public double getDamage() {
        return damage.get();
    }

    public DoubleProperty damageProperty() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage.set(damage);
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

    public String getProjectileImageName() {
        return projectileImageName;
    }

    public void setProjectileImageName(String projectileImageName) {
        this.projectileImageName = projectileImageName;
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

    public int getAmmo() {
        return ammo.get();
    }

    public IntegerProperty ammoProperty() {
        return ammo;
    }

    final public void setAmmo(int ammo) {
        this.ammo.set(ammo);
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    final public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public MoveBehaviour getProjectileMoveBehaviour() {
        return projectileMoveBehaviour;
    }

    public void setProjectileMoveBehaviour(MoveBehaviour projectileMoveBehaviour) {
        this.projectileMoveBehaviour = projectileMoveBehaviour;
    }

    public Weapon(GameObject owner, double x, double y, Integer id) {
        super(owner, x, y, true, id);
        setMoveBehaviour(new RotateBehaviour(this, -1));
//        getHitBox().setOpacity(0);
    }

    public void generateProjectileCollisionType(){
        setProjectileCollisionEffect(new DamageEffect(damageProperty(), 1.1, getOwner().getID(), null));
        setProjectileCollisionBehaviour(new ObjIgnoredBehaviour(new Class[]{Weapon.class}, new DisappearBehaviour(null)));
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

//    public String getProjectileImageName() {
//        return projectileImageName;
//    }
//
//    public void setProjectileImageName(String projectileImageName) {
//        this.projectileImageName = projectileImageName;
//    }

    public double getProjectileHitBoxSize() {
        return projectileHitBoxSize;
    }

    final public void setProjectileHitBoxSize(double size) {
        this.projectileHitBoxSize = size;
    }


    public void setFireSoundFX(String fireSoundFX) {
        this.fireSoundFX = fireSoundFX;
    }

    public String getFireSoundFX() {
        return fireSoundFX;
    }

    public Projectile forceFire(Integer projectileID){
        generateProjectileCollisionType();
        if (fireSoundFX != null) {
            SoundStudio.getInstance().playAudio(fireSoundFX, getTranslateX(), getTranslateY(), 150*scope, 150*scope);
        }
        Projectile projectile = new Projectile(this, getRange(), new Circle(projectileHitBoxSize, projectileColor), projectileImageName, projectileID);
        if (MadBalls.isHost()){
            MadBalls.getMultiplayerHandler().sendData(new FireData(getID(), projectile.getID(), -1));
        }
        checkOutOfAmmo();
        return projectile;
    }
    
    public void attack(long now){
        if (!MadBalls.isHost()) return;
        if ((now - getLastShotTime()) / 1_000_000_000.0  >  1  / fireRate){
            forceFire(-1);
            setLastShotTime(now);
        }
    }

    public boolean checkOutOfAmmo(){
        if (getAmmo() == 0 || getAmmo() < -1){
//            ((Ball)getOwner()).setWeapon(Pistol.class);
            if (MadBalls.isHost()){
                ((Ball)getOwner()).setWeapon(Pistol.class, -1);
                MadBalls.getMultiplayerHandler().sendData(new GetWeaponData(getOwner().getID(), Pistol.class.getName(), ((Ball)getOwner()).getWeapon().getID()));
            }
        }
        return getAmmo() == 0 || getAmmo() < -1;
    }
    
    @Override
    public void updateUnique(long now) {
//        System.out.println(owner.getTranslateY());
//        System.out.println(getTranslateY());
        if (getMoveBehaviour().isMousePressed()) attack(now);
    }
}
