/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import madballs.item.Spawner;
import madballs.map.Map;

/**
 *
 * @author Caval
 */
public class Environment {
    private static Environment instance = new Environment();
    private java.util.Map<Integer, GameObject> gameObjects;
    private int currentObjID = 0;
    private LongProperty lastUpdateTime = new SimpleLongProperty(0);
    private Spawner itemSpawner;
    private Group display;
    private Map map;
    private Ground ground;
    private Quadtree quadtree;
    private Scene scene;
    private int updateIndex = 0;

    public Group getDisplay() {
        return display;
    }
    
    public int getNumObjects(){
        return gameObjects.size();
    }
    
    public GameObject getObject(Integer id){
        return gameObjects.get(id);
    }
    
//    public int getObjectIndex(GameObject object){
//        return gameObjects.indexOf(object);
//    }
    
    public Spawner getItemSpawner() {
      return itemSpawner;
    }
    public long getLastUpdateTime() {
        return lastUpdateTime.get();
    }
    
    public int getUpdateIndex(){
        return updateIndex;
    }
    
    public Ground getGround() {
        return ground;
    }
    
    final AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateIndex++;
//            if (MadBalls.isHost() && (now - lastUpdateTime.get()) < 1000000000/120 ){
//                return;
//            }
//            if (!MadBalls.isHost() && (now - lastUpdateTime.get()) < 1000000000/800 ){
//                return;
//            }
            lastUpdateTime.set(now);
            update(now);
        }
    };
    
    /**
     * check through all game objs in the environment to see which obj has collided with one another
     */
    private void update(long now){
        MadBalls.getMultiplayerHandler().checkWinner();
//        boolean isHost = MadBalls.getMultiplayerHandler().getLocalPlayer().isHost();
      
        java.util.Map<Integer, GameObject> copiedGameObjects = new HashMap<>(gameObjects);
        ArrayList<Integer> deadObjIDs = new ArrayList<>();
//        copiedGameObjects.addAll(gameObjects.subList(0, gameObjects.size()));
        quadtree.clear();
        
        for (GameObject obj : copiedGameObjects.values()){
            obj.update(now);
//            obj.updateBoundsRectangle();
            if (obj.isDead()) {
                deadObjIDs.add(obj.getID());
            }
            else {
                quadtree.insert(obj);
            }
        }
        
        for (Integer id : deadObjIDs){
            gameObjects.remove(id);
            copiedGameObjects.remove(id);
        }
        
//        copiedGameObjects = new ArrayList<>(gameObjects);
//        if (!isHost) return;
        //spawn items
        if (MadBalls.isHost()) itemSpawner.spawn(now);
        List<GameObject> collidableObjects = new ArrayList();
        ArrayList<GameObject> checked = new ArrayList<>();
//        ArrayList<GameObject> collidedObjects = new ArrayList<>();
        
//        boolean isUncollided = false;
        for (GameObject checking : copiedGameObjects.values()){
            if (checking.isDead() || checking instanceof Obstacle) continue;
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
    
    private Environment(){
        this.itemSpawner = new Spawner(this);
        gameObjects = new HashMap<>();
    }
    
    public void setDisplay(Group display){
        this.display = display;
        ground = new Ground(this, 0, 0);
    }
    
    public static Environment getInstance(){
        return instance;
    }
    
    public void loadMap(Map map){
        this.map = map;
        String[][] mapArray = map.getMAP_ARRAY();
        //add the obstacles
        for (int i = 0; i < map.getNumRows(); i++){
            for (int j = 0; j < map.getNumColumns(); j++){
              System.out.print(  mapArray[i][j]);
                if (mapArray[i][j] != null && (mapArray[i][j]).equals("+")) {
//                  System.out.println("Create OBJ " + horizontalUnit + " " + verticalUnit);
                    new Obstacle(this,j * map.getColumnWidth(), i * map.getRowHeight(), map.getObstacleSize(), map.getObstacleSize());
                }
            }
          System.out.println("\n");
        }
        quadtree = new Quadtree(0, new Rectangle(-25, -25, map.getWidth() + 25, map.getHeight() + 25));
    }
    
    public void startAnimation(){
        animation.start();
        MadBalls.getNavigation().showInterupt("", "Game started", "Let's rock and roll!", false);
    }
    
    /**
     * add new obj to the environment
     * @param obj 
     */
    public void registerGameObj(GameObject obj, boolean shouldAddDisplay){
        Integer newID = new Integer(currentObjID++);
        obj.setID(newID);
        gameObjects.put(newID++, obj);
//        System.out.println(getObjectIndex(obj));
        if (shouldAddDisplay) display.getChildren().add(obj.getDisplay());
    }
    
    /**
     * remove an obj from the environment
     * @param obj 
     */
    public void removeGameObj(GameObject obj){
//        gameObjects.remove(obj.getID());
        display.getChildren().remove(obj.getDisplay());
    }
}
