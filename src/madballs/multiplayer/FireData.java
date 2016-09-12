/*Client.java:36
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 * the Data to inform the attack of a Weapon
 * @author caval
 */
public class FireData extends Data{
    private Integer weaponID;
    private Integer projectileID;
    private double direction;

    public Integer getWeaponID() {
        return weaponID;
    }

    public int getProjectileID() {
        return projectileID;
    }

    public double getDirection() {
        return direction;
    }

    public FireData(Integer weaponID, Integer projectileID, double direction) {
        super("fire");
        this.weaponID = weaponID;
        this.projectileID = projectileID;
        this.direction = direction;
    }
    
}
