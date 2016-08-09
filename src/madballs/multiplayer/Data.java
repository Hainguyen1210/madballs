/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.multiplayer;

import java.io.Serializable;

/**
 *
 * @author caval
 */
public class Data implements Serializable{
    private String type;
    
    public String getType(){
        return type;
    }
    
    public Data(String type){
        this.type = type;
    }
}
