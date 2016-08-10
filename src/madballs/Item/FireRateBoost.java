/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.collision.Boost.FireRate;
import madballs.item.Item;

/**
 *
 * @author chim-
 */
public class FireRateBoost extends Item{

    public FireRateBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay);
        setCollisionEffect(new FireRate(null, 1.5, 5));
    }

    @Override
    public void setDisplayComponents() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setColor(Paint.valueOf("green"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
    }
    
}
