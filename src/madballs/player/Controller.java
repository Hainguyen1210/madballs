/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.player;

import javafx.scene.input.KeyCode;
import madballs.MoveBehaviour;
import madballs.RotateBehaviour;
import madballs.StraightMove;

/**
 *
 * @author caval
 */
public class Controller {
    private Player player;
    
    public Controller(Player player){
        this.player = player;
    }
    
    public void handleKey(MultiplePressedKeysEventHandler.MultiKeyEvent ke){
        StraightMove ballMoveBehaviour = (StraightMove) player.getBall().getMoveBehaviour();
//                try {
        if (!((ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) && (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)))) {
            ballMoveBehaviour.setVelocityX(0);
//                        MadBalls.out.writeObject("x 0");
        }
        if (!((ke.isPressed(KeyCode.UP)  || ke.isPressed(KeyCode.W)) && (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)))) {
            ballMoveBehaviour.setVelocityY(0);
//                        MadBalls.out.writeObject("y 0");
        }


        if (ke.isPressed(KeyCode.LEFT)  || ke.isPressed(KeyCode.A)) {
            ballMoveBehaviour.setVelocityX(-ballMoveBehaviour.getSpeed());
//                        MadBalls.out.writeObject("x -");
        }
        if (ke.isPressed(KeyCode.RIGHT) || ke.isPressed(KeyCode.D)) {
            ballMoveBehaviour.setVelocityX(ballMoveBehaviour.getSpeed());
//                        MadBalls.out.writeObject("x +");
        }

        if (ke.isPressed(KeyCode.UP) || ke.isPressed(KeyCode.W)) {
            ballMoveBehaviour.setVelocityY(-ballMoveBehaviour.getSpeed());
//                        MadBalls.out.writeObject("y -");
        }
        if (ke.isPressed(KeyCode.DOWN) || ke.isPressed(KeyCode.S)) {
            ballMoveBehaviour.setVelocityY(ballMoveBehaviour.getSpeed());
//                        MadBalls.out.writeObject("y +");
        }
//                }
//                catch (IOException ex){
//                    
//                }
    }
    
    public void handleMouse(MouseKeyEventHandler.MouseKeyEvent event){
        RotateBehaviour weaponRotateBehaviour = (RotateBehaviour) player.getBall().getWeapon().getMoveBehaviour();
//        System.out.println("ahihi");
//        System.out.println(player.getBall().getWeapon().getClass());
        weaponRotateBehaviour.setTargetX(event.getMouseX());
        weaponRotateBehaviour.setTargetY(event.getMouseY());
        weaponRotateBehaviour.setMousePressed(event.isPressed());
    }
}
