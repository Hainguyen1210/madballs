/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
    private Ground ground;
    private Quadtree quadtree;

    public long getLastUpdateTime() {
        return lastUpdateTime.get();
    }

    public Ground getGround() {
        return ground;
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
        ArrayList<GameObject> copiedGameObjects = new ArrayList<>();
        copiedGameObjects.addAll(gameObjects.subList(0, gameObjects.size()));
        
        quadtree.clear();
        
        for (GameObject obj : copiedGameObjects){
            obj.update(now);
            obj.updateBoundsRectangle();
            quadtree.insert(obj);
        }
        List<GameObject> collidableObjects = new ArrayList();
//        ArrayList<GameObject> checked = new ArrayList<>();
        ArrayList<GameObject> uncollidedObjects = new ArrayList<>();
        
//        boolean isUncollided = false;
        for (GameObject checking : copiedGameObjects){
            collidableObjects.clear();
            quadtree.retrieve(collidableObjects, checking.getBoundsRectangle());
                for (GameObject target : collidableObjects){
                    if (target != checking
                            && !target.hasChild(checking) && !target.hasOwner(checking)){
                        if (!checking.checkCollisionWith(target)) {
                            uncollidedObjects.add(target);
                            uncollidedObjects.add(checking);
                        }
                    }
                }
//                checked.add(checking);
        }
        
        for (GameObject uncollided : uncollidedObjects){
            uncollided.setOldDirection(Math.toRadians(uncollided.getRotateAngle()));
            uncollided.setOwnerOldX(uncollided.getOwnerTranslateX());
            uncollided.setOwnerOldY(uncollided.getOwnerTranslateY());
        }
    }
    
    public Environment(Pane display, Map map){
        this.root = display;
        this.map = map;
        quadtree = new Quadtree(0, new Rectangle(0, 0, MadBalls.RESOLUTION_X, MadBalls.RESOLUTION_Y));
        gameObjects = new ArrayList<>();
        ground = new Ground(this, 0, 0);
        
        
        for (int i = 15; i >= 0; i --){
            for (int j = 0; j < 9; j++){
                if (map.getMAP_ARRAY()[i][j] == 1) new Obstacle(this, i * 50, j * 50, 50, 50);
            }
        }
//        new Obstacle(this, 15 * 50, 8 * 50, 50, 50);
        
        
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
