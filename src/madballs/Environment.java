/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.layout.Pane;
import madballs.Map.Map;

/**
 *
 * @author Caval
 */
public class Environment {
    private ArrayList<GameObject> gameObjects;
    private LongProperty lastUpdateTime = new SimpleLongProperty(0);
    private Pane root;
    private Map map;

    public long getLastUpdateTime() {
        return lastUpdateTime.get();
    }
    
    final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            lastUpdateTime.set(now);
            update(now);
        }
    };
    
    /**
     * check through all game objs in the environment to see which obj has collided with one another
     */
    private void update(long now){
        // create the arraylist storing the checked collidables
//        ArrayList<GameObject> checked = new ArrayList<>();
        
        // loop through all the collidables in the environment
        for (GameObject checking : gameObjects){
//            if (!checked.contains(checking)) {
                for (GameObject target : gameObjects){
                    checking.update(now);
                    if (target != checking && !target.hasChild(checking) && !target.hasOwner(checking)){
//                        System.out.println(checking instanceof Weapon);
//                        System.out.println(target instanceof  Obstacle);
                        checking.checkCollisionWith(target);
                    }
                }
                
//                checked.add(checking);
//            }
        }
    }
    
    public Environment(Pane display, Map map){
        this.root = display;
        
        
        gameObjects = new ArrayList<>();
        this.map = map;
        for (int i = 15; i >= 0; i --){
            for (int j = 0; j < 9; j++){
                if (map.getMAP_ARRAY()[i][j] == 1) new Obstacle(this, i * 50, j * 50, 50, 50);
            }
        }
        
        
        animation.start();
    }
    
    /**
     * add new obj to the environment
     * @param obj 
     */
    public void registerGameObj(GameObject obj, boolean shouldAddDisplay){
        gameObjects.add(obj);
        if (shouldAddDisplay) root.getChildren().add(obj.getDisplay());
    }
    
    /**
     * remove an obj from the environment
     * @param obj 
     */
    public void removeGameObj(GameObject obj){
        gameObjects.remove(obj);
        root.getChildren().remove(obj.getDisplay());
    }
}
