/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

/**
 *
 * @author Caval
 */
public class Environment {
    private ArrayList<GameObject> gameObjects;
    private Pane display;
    
    final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update(now);
        }
    };
    
    /**
     * check through all collidables in the environment to see which collidable has collided with one another
     */
    private void update(long now){
        // create the arraylist storing the checked collidables
        ArrayList<GameObject> checked = new ArrayList<>();
        
        // loop through all the collidables in the environment
        for (GameObject checking : gameObjects){
            if (!checked.contains(checking)) {
                for (GameObject target : gameObjects){
                    checking.getMoveBehaviour().move(now);
                    if (target != checking){
                        checking.checkCollisionWith(target);
                    }
                }
                
                checked.add(checking);
            }
        }
    }
    
    public Environment(Pane display){
        this.display = display;
        gameObjects = new ArrayList<>();
        animation.start();
    }
    
    public void registerGameObj(GameObject obj){
        gameObjects.add(obj);
        display.getChildren().addAll(obj.getHitBox(),obj.getImage());
    }
}
