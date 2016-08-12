/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.Item;

import madballs.Environment;
import madballs.effectState.DamageBoosted;
import madballs.item.EffectItem;

public class DamageBoost extends EffectItem{
    public DamageBoost(Environment environment, double x, double y, boolean isSettingDisplay) {
        super(environment, x, y, isSettingDisplay, new DamageBoosted(null, null, 5, 2));
    }
}
