/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 *
 * @author caval
 */
public class GetWeaponData extends Data{
    private int ballIndex;
    private String weaponClassName;

    public int getBallIndex() {
        return ballIndex;
    }

    public String getWeaponClassName() {
        return weaponClassName;
    }

    public GetWeaponData(int ballIndex, String weaponClassName) {
        super("get_weapon");
        this.ballIndex = ballIndex;
        this.weaponClassName = weaponClassName;
    }
    
}
