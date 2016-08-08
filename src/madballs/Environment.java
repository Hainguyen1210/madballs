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
import madballs.Item.Item;
import madballs.Item.Spawner;
import madballs.Item.SpeedBoost;
import madballs.Map.Map;

/**
 *
 * @author Caval
 */
public class Environment {
    private ArrayList<GameObject> gameObjects;
    private LongProperty lastUpdateTime = new SimpleLongProperty(0);
    private LongProperty lastItemSpawnTime = new SimpleLongProperty(0);
    private Spawner spawner;
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
      //spawn items
      if((now - lastItemSpawnTime.get()) / 1000000000.0 > 3){
        lastItemSpawnTime.set(now);
//        spawner.randomSpawn();
      }
      
        ArrayList<GameObject> copiedGameObjects = new ArrayList<>(gameObjects);
//        copiedGameObjects.addAll(gameObjects.subList(0, gameObjects.size()));
        
        quadtree.clear();
        
        for (GameObject obj : copiedGameObjects){
            obj.update(now);
//            obj.updateBoundsRectangle();
            quadtree.insert(obj);
        }
        List<GameObject> collidableObjects = new ArrayList();
        ArrayList<GameObject> checked = new ArrayList<>();
//        ArrayList<GameObject> collidedObjects = new ArrayList<>();
        
//        boolean isUncollided = false;
        for (GameObject checking : copiedGameObjects){
            if (checking instanceof Obstacle) continue;
            collidableObjects.clear();
            quadtree.retrieve(collidableObjects, checking);
            for (GameObject target : collidableObjects){
//                if (checking instanceof Ball)System.out.println(target.getClass() + " x: " + target.getDisplay().getBoundsInParent().getMinX() + "; y: " + target.getDisplay().getBoundsInParent().getMinY());
//  if(checking instanceof Item){System.out.println("CHECKING ITEM");}
                if (target != checking && !checked.contains(target) && !target.hasChild(checking) && !target.hasOwner(checking)){
                    checking.checkCollisionWith(target);
//                        if (checking.checkCollisionWith(target)) {
//                            collidedObjects.add(target);
//                            collidedObjects.add(checking);
//                            GameObject owner = target.getOwner();
//                            while (owner != null){
//                                collidedObjects.add(owner);
//                                owner = owner.getOwner();
//                            }
//                            owner = checking.getOwner();
//                            while (owner != null){
//                                collidedObjects.add(owner);
//                                owner = owner.getOwner();
//                            }
//                        }
                }
            }
                checked.add(checking);
        }
        
//        for (GameObject obj : copiedGameObjects){
//            if (collidedObjects.contains(obj)) return;
////            obj.setOldDirection(Math.toRadians(obj.getRotateAngle()));
////            if (obj instanceof Ball) {
////                System.out.println("");
////                System.out.println(obj.getLastStableX());
////                System.out.println(obj.getOldX());
////            }
//            obj.setLastStableX(obj.getTranslateX());
//            obj.setLastStableY(obj.getTranslateY());
//        }
    }

  public Map getMap() {
    return map;
  }
    

    public Environment(Pane display, Map map){
      this.spawner = new Spawner(this);
        this.root = display;
        this.map = map;
        quadtree = new Quadtree(0, new Rectangle(-25, -25, MadBalls.RESOLUTION_X + 25, MadBalls.RESOLUTION_Y + 25));
        gameObjects = new ArrayList<>();
        ground = new Ground(this, 0, 0);
        
        //add the obstacles 
        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 30; j++){
                if (map.getMAP_ARRAY()[i][j] == 1) new Obstacle(this, 
                        j * map.getLENGTH()/30, i * map.getHEIGHT()/20,
                        30, 30); 
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
