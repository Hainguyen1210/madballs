/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Collision.Boost.Heal;
import madballs.Environment;

/**
 *
 * @author chim-
 */
public class HealBoost extends Item{
    public HealBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay);
        setCollisionEffect(new Heal(null,100,0));
    }

    @Override
    public void setDisplayComponents() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setColor(Paint.valueOf("red"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
    }
    
}
