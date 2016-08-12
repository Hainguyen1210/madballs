/*Client.java:36
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

/**
 *
 * @author caval
 */
public class FireData extends Data{
    private int weaponIndex;

    public int getWeaponIndex() {
        return weaponIndex;
    }
    
    public FireData(int weaponIndex) {
        super("fire");
        this.weaponIndex = weaponIndex;
    }
    
}
