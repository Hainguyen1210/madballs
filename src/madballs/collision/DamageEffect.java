/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.collision;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Shape;
import madballs.GameObject;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * a collision effect that deals damage to the target with a time interval
 * @author Caval
 */
public class DamageEffect extends StackedCollisionEffect{
    private DoubleProperty damage = new SimpleDoubleProperty();
    private Integer ballID;
    private double applyInterval;
    private Map<GameObject, Long> damageTimesMap = new WeakHashMap<>();

    public Integer getBallID() {
        return ballID;
    }

    public DamageEffect(double amount, double applyInterval, Integer ballID, StackedCollisionEffect effect){
        super(effect);
        this.damage.set(amount);
        this.ballID = ballID;
        this.applyInterval = applyInterval;
    }

    public DamageEffect(DoubleProperty amount, double applyInterval, Integer ballID, StackedCollisionEffect effect){
        super(effect);
        damage.bind(amount);
        this.ballID = ballID;
        this.applyInterval = applyInterval;
    }

    @Override
    public void affect(GameObject source, GameObject target, Shape collisionShape) {
        long now = target.getEnvironment().getLastUpdateTime();
        if (!damageTimesMap.containsKey(target)) {
            target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
            damageTimesMap.put(target, now);
        }
        else if ((now - damageTimesMap.get(target)) / 1000000000 > applyInterval) {
            target.getCollisionPassiveBehaviour().getAffected(source, target, this, collisionShape);
            damageTimesMap.replace(target, now);
        }
        if (wrappedEffect != null) wrappedEffect.affect(source, target, collisionShape);
    }

    public double getDamage() {
        return damage.get();
    }
    
}
