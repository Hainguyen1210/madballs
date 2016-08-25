/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs.player;

import javafx.scene.input.KeyCode;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.multiplayer.MapData;
import madballs.multiplayer.PlayerData;
import madballs.scenes.Navigation;
import madballs.scenes.SceneManager;
import madballs.wearables.Weapon;

import java.util.ArrayList;

/**
 *
 * @author caval
 */
public class Controller {
    private Player player;
    private double sceneWidth, sceneHeight;

    public double getSceneWidth() {
        return sceneWidth;
    }

    public void setSceneWidth(double sceneWidth) {
        this.sceneWidth = sceneWidth;
    }

    public double getSceneHeight() {
        return sceneHeight;
    }

    public void setSceneHeight(double sceneHeight) {
        this.sceneHeight = sceneHeight;
    }

    public Controller(Player player){
        this.player = player;
    }
    
    public void handleKey(MultiplePressedKeysEventHandler.MultiKeyEvent ke){
        if (ke.isPressed(KeyCode.I) && ke.isPressed(KeyCode.O) && ke.isPressed(KeyCode.P)) {
            if (MadBalls.isHost() && player == MadBalls.getMultiplayerHandler().getLocalPlayer()){
                if (ke.isPressed(KeyCode.U)){
                    Map map = Map.chooseMap();
                    MadBalls.getMultiplayerHandler().sendData(new MapData(map.getMapNumber()));
                    for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                        player.setTeamNum(0);
                        MadBalls.getMultiplayerHandler().sendData(new PlayerData(player));
                    }
                    MadBalls.loadTempMap(map);
                }
                else {
                    MadBalls.getMultiplayerHandler().newMatch(true);
                }
                ke.clearBuffer();
            }
        }

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

        SceneManager.getInstance().getScoreBoardContainer().setVisible(ke.isPressed(KeyCode.TAB));

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
        if (ke.isPressed(KeyCode.SPACE)) {
            RotateBehaviour weaponRotateBehaviour = (RotateBehaviour) player.getBall().getWeapon().getMoveBehaviour();
            weaponRotateBehaviour.setMousePressed(true);
        }
//                }
//                catch (IOException ex){
//                    
//                }
    }
    
    public void handleMouse(MouseKeyEventHandler.MouseKeyEvent event){
        Weapon playerWeapon = player.getBall().getWeapon();
        RotateBehaviour weaponRotateBehaviour = (RotateBehaviour) playerWeapon.getMoveBehaviour();
//        System.out.println("ahihi");
//        System.out.println(player.getBall().getWeapon().getClass());
//        weaponRotateBehaviour.setTargetX(event.getMouseX());
//        weaponRotateBehaviour.setTargetY(event.getMouseY());
        double scale = event.getScale();
        double yDiff = event.getMouseY() - sceneHeight/2 - playerWeapon.getOwnerDiffY()*scale;
        double xDiff = event.getMouseX() - sceneWidth/2 - playerWeapon.getOwnerDiffX()*scale;
        double newDirection = Math.atan2(yDiff, xDiff);
//        System.out.println(Math.toDegrees(newDirection));
        weaponRotateBehaviour.setNewDirection(newDirection);
        weaponRotateBehaviour.setMousePressed(event.isPressed());
    }
}
