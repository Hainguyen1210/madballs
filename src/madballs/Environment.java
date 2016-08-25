/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import madballs.item.Spawner;
import madballs.map.Map;
import madballs.player.Player;
import madballs.scenes.Navigation;

/**
 *
 * @author Caval
 */
public class Environment {
    private java.util.Map<Integer, GameObject> gameObjects, deadGameObjects;
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

    public int getCurrentObjID() {
        return currentObjID;
    }
    
    public void setCurrentObjID(Integer id){
        this.currentObjID = id;
    }
    
    private final AnimationTimer animation = new AnimationTimer() {
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
      
        java.util.Map<Integer, GameObject> copiedGameObjects = new WeakHashMap<>(gameObjects);
//        ArrayList<Integer> deadObjIDs = new ArrayList<>();
        quadtree.clear();
        
        for (GameObject obj : copiedGameObjects.values()){
            obj.update(now);
//            obj.updateBoundsRectangle();
            if (obj.isDead()) {
//                System.out.println("dead" + gameObjects.containsKey(obj.getID()));
//                System.out.println(currentObjID);
//                System.out.println(gameObjects.size());
                deadGameObjects.put(obj.getID(), obj);
                gameObjects.remove(obj.getID());
//                System.out.println(gameObjects.size());
            }
            else {
                quadtree.insert(obj);
            }
        }

//        for (Integer id : deadObjIDs){
//            System.out.println("dead" + gameObjects.containsKey(id));
//            System.out.println(currentObjID);
//            System.out.println(gameObjects.size());
//            gameObjects.remove(id);
//            System.out.println(gameObjects.size());
//            copiedGameObjects.remove(id);
//        }
        
        copiedGameObjects = new WeakHashMap<>(gameObjects);
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
    
    public Environment(){
        this.itemSpawner = new Spawner(this);
        gameObjects = new WeakHashMap<>();
        deadGameObjects = new WeakHashMap<>();
    }
    
    public void setDisplay(Group display){
        this.display = display;
        ground = new Ground(this, 0, 0, -1);
    }

    public void loadMap(Map map) {
        this.map = map;
        String[][] mapArray = map.getMAP_ARRAY();
        //add the obstacles

        int boxSize = map.getObstacleSize();
        int currentBoxSizeRow = 0, locationRowX = 0, locationRowY = 0; //define start point and size
        int currentBoxSizeCol = 0, locationColX = 0, locationColY = 0;
        boolean isStartedRow = false, isStartedCol = false;

        //render rows
        for(int row = 0; row < map.getNumRows();row++){
            for (int col = 0; col < map.getNumColumns(); col++){
                System.out.print( mapArray[row][col] );
                if (mapArray[row][col] != null && (mapArray[row][col]).equals("x")) {
                    if (!isStartedRow){
                        locationRowX = col*map.getColumnWidth(); locationRowY = row*map.getRowHeight(); //set start point
                        isStartedRow = true;
                    }
                    currentBoxSizeRow += boxSize;
                    // render last row right away
                    if (row == map.getNumRows()-1
                            ) new Obstacle(this, locationRowX, locationRowY, currentBoxSizeRow, boxSize, -1);
                } else if (mapArray[row][col] != null && !(mapArray[row][col]).equals("x")){
                    if (isStartedRow){
                        new Obstacle(this, locationRowX, locationRowY, currentBoxSizeRow, boxSize, -1); //end point
                        System.out.print("#");
                        currentBoxSizeRow = 0;
                        isStartedRow = false;
                    }
                }
            } System.out.println();
        }
        //render columns
        for (int col = 0; col < map.getNumColumns(); col++) {
            for (int row = 0; row < map.getNumRows(); row++) {
                if (mapArray[row][col] != null && (mapArray[row][col]).equals("+")) {
                    if (!isStartedCol){
                        locationColX = col*map.getColumnWidth(); locationColY = row*map.getRowHeight(); //set start point
                        isStartedCol = true;
                    }
                    currentBoxSizeCol += boxSize;
                } else if (mapArray[row][col] != null && !(mapArray[row][col]).equals("+")){
                    if (isStartedCol){
                        new Obstacle(this, locationColX, locationColY, boxSize, currentBoxSizeCol, -1); //end point
                        currentBoxSizeCol = 0;
                        isStartedCol = false;
                    }
                }
            }
        }

        quadtree = new Quadtree(0, new Rectangle(-25, -25, map.getWidth() + 25, map.getHeight() + 25));
    }
    
    public void startAnimation(){
        animation.start();
//        Navigation.getInstance().showInterupt("", "Game started", "Let's rock and roll!", false);
    }

    public void stopAnimation(){
        animation.stop();
    }
    
    /**
     * add new obj to the environment
     * @param obj 
     */
    public void registerGameObj(GameObject obj, boolean shouldAddDisplay, Integer id){
        if (id == -1){
            id = currentObjID;
        }
        obj.setID(id);
        gameObjects.put(id, obj);
//        System.out.println(getObjectIndex(obj));
        if (shouldAddDisplay) display.getChildren().add(obj.getDisplay());
        currentObjID = id + 1;
//        System.out.println("z" + obj.getDisplay().getTranslateZ());
    }
    
    /**
     * remove an obj from the environment
     * @param obj 
     */
    public void removeGameObj(GameObject obj){
        display.getChildren().remove(obj.getDisplay());
    }

    public GameObject resurrectGameObj(Integer id){
        synchronized (gameObjects){
            GameObject obj = deadGameObjects.get(id);
            if (obj != null){
                gameObjects.put(id, obj);
                if (!display.getChildren().contains(obj.getDisplay())){
                    display.getChildren().add(obj.getDisplay());
                }
                deadGameObjects.remove(id);
            }
            System.out.println("resurrect");
            return obj;
        }
    }
}
