/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import javafx.scene.input.KeyEvent;

/**
 *
 * @author caval
 */
public class KeyInputData extends Data{
    private KeyEvent keyEvent;

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }
    
    public KeyInputData(KeyEvent ke) {
        super("input_key");
        keyEvent = ke;
    }
    
}
