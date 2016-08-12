/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import madballs.Environment;
import madballs.effectState.FireRateBoosted;
import madballs.item.EffectItem;

/**
 *
 * @author chim-
 */
public class FireRateBoost extends EffectItem{

    public FireRateBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay, new FireRateBoosted(null, null, 5, 1.5));
    }
}
