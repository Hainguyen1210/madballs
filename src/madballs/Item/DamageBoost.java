/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import madballs.Environment;
import madballs.collision.Boost.Damage;
import madballs.item.Item;

public class DamageBoost extends Item{

    public DamageBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay);
        setCollisionEffect(new Damage(null, 2, 5));
    }

    @Override
    public void setDisplayComponents() {
    setColor(Paint.valueOf("blue"));
    setSize(15);
    setHitBox(new Circle(getSize(), getColor()));
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
