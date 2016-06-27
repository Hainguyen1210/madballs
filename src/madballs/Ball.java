/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Caval
 */
public class Ball extends GameObject{
    private Weapon weapon;
    private DoubleProperty hp = new SimpleDoubleProperty(100);

    public Ball(Environment environment, double a, double b) {
        super(environment, a , b);
        setMoveBehaviour(new StraightMove(this));
    }
}
