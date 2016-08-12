/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import javafx.scene.shape.Shape;

/**
 *
 * @author caval
 */
public class CollisionData extends Data{
    private int firstIndex, secondIndex;
    
    public int getFirstIndex() {
        return firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public CollisionData(int firstIndex, int secondIndex, Shape collisionShape) {
        super("collision");
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }
}
