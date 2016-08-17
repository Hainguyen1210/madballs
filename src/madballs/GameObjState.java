/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.io.Serializable;

import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Weapon;

/**
 *
 * @author caval
 */
public class GameObjState implements Serializable{
    private Integer objID;
    private double translateX;
    private double translateY;
    private double oldX, oldY;
    private double velocityX, velocityY;
    private double targetX, targetY;
    private double direction;
    private double oldDirection;
    private double hp;
    private boolean isDead;
    private double speed;
    private double damage, fireRate;
    private int updateIndex;

    public Integer getObjID() {
        return objID;
    }

    public double getDamage() {
        return damage;
    }

    public double getFireRate() {
        return fireRate;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getUpdateIndex(){
        return updateIndex;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public double getDirection() {
        return direction;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public double getOldDirection() {
        return oldDirection;
    }

    public double getHp() {
        return hp;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public boolean isSimilarTo(GameObjState state){
        return (
                state.translateX == translateX
                && state.translateY == translateY
                && state.oldX == oldX
                && state.oldY == oldY
                && state.direction == direction
                && state.oldDirection == oldDirection
                && state.hp == hp
                && state.isDead == isDead
                && state.speed == speed
                && state.targetX == targetX
                && state.targetY == targetY
                && state.damage == damage
                && state.fireRate == fireRate
                && state.velocityX == velocityX
                && state.velocityY == velocityY
            );
    }
    
    public GameObjState(GameObject obj){
        objID = obj.getID();
        updateIndex = obj.getEnvironment().getUpdateIndex();
        translateX = obj.getTranslateX();
        translateY = obj.getTranslateY();
        direction = Math.toRadians(obj.getRotateAngle());
        oldX = obj.getOldX();
        oldY = obj.getOldY();
        oldDirection = obj.getOldDirection();
        hp = obj.getHpValue();
        isDead = obj.isDead();
        if (obj.getMoveBehaviour() != null){
            speed = obj.getMoveBehaviour().getSpeed();
            if (obj.getMoveBehaviour() instanceof RotateBehaviour){
                RotateBehaviour rotateBehaviour = (RotateBehaviour) obj.getMoveBehaviour();
                targetX = rotateBehaviour.getTargetX();
                targetY = rotateBehaviour.getTargetY();
            }
            else if (obj.getMoveBehaviour() instanceof StraightMove){
                StraightMove straightMove = (StraightMove)obj.getMoveBehaviour();
                velocityX = straightMove.getVelocityX();
                velocityY = straightMove.getVelocityY();
            }
        }
        if (obj instanceof Weapon){
            Weapon weapon = (Weapon) obj;
            damage = weapon.getDamage();
            fireRate = weapon.getFireRate();
        }
    }
}
