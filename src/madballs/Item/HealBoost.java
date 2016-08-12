/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import madballs.Environment;
import madballs.effectState.HealBoosted;
import madballs.item.EffectItem;

/**
 *
 * @author chim-
 */
public class HealBoost extends EffectItem{
    public HealBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay, new HealBoosted(null, null, 0, 0));
    }
}
