/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 * the Data about a Ball picking up a new Weapon
 * @author caval
 */
public class GetWeaponData extends Data{
    private Integer ballID;
    private String weaponClassName;
    private Integer weaponID;

    public Integer getBallID() {
        return ballID;
    }

    public String getWeaponClassName() {
        return weaponClassName;
    }

    public Integer getWeaponID() {
        return weaponID;
    }

    public GetWeaponData(Integer ballID, String weaponClassName, Integer weaponID) {
        super("get_weapon");
        this.ballID = ballID;
        this.weaponClassName = weaponClassName;
        this.weaponID = weaponID;
    }
    
}
