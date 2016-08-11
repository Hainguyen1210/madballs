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
public class DeadData extends Data{
    private int objectIndex;

    public int getObjectIndex() {
        return objectIndex;
    }
    
    public DeadData(int index) {
        super("dead");
        this.objectIndex = index;
    }
    
}
