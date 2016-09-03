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
import madballs.AI.BotPlayer;
import madballs.item.Item;
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
    private int updateIndex = 0;

    public Quadtree getQuadtree() {
        return quadtree;
    }

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
//            if (MadBalls.isHost() && (now - lastUpdateTime.get()) < 1000000000/120 ){
//                return;
//            }
//            if (!MadBalls.isHost() && (now - lastUpdateTime.get()) < 1000000000/100 ){
//                return;
//            }
            updateIndex++;
            lastUpdateTime.set(now);
            update(now);
        }
    };
    
    /**
     * check through all game objs in the environment to see which obj has collided with one another
     */
    private void update(long now){
        if (MadBalls.isHost()) MadBalls.getGameMode().manage(now);

        ArrayList<GameObject> copiedGameObjects = new ArrayList<>(gameObjects.values());
        ArrayList<GameObject> shouldBeCheckedGameObjects = new ArrayList<>();
        quadtree.clear();

        // update objects
        for (GameObject obj : copiedGameObjects){
            obj.update(now);
            if (obj.isDead()) {
                deadGameObjects.put(obj.getID(), obj);
                gameObjects.remove(obj.getID());
            }
            else {
                if (!obj.isDead()
                        && obj.isMobile()){
                    shouldBeCheckedGameObjects.add(obj);
                }
                quadtree.insert(obj);
            }
        }

//        if (!isHost) return;
        // spawn items
        if (MadBalls.isHost()) itemSpawner.spawn(now);

        // collision checking
        List<GameObject> collidableObjects = new ArrayList();
        ArrayList<GameObject> checked = new ArrayList<>();

        for (GameObject checking : shouldBeCheckedGameObjects){
            if (!MadBalls.isHost() && !MadBalls.getMultiplayerHandler().getLocalPlayer().getRelevancy(checking.getTranslateX(), checking.getTranslateY(), 250, 250)) {
                continue;
            }
            collidableObjects.clear();
            quadtree.retrieve(collidableObjects, checking);
            for (GameObject target : collidableObjects){
//            for (GameObject target : copiedGameObjects.values()){
                if (target != checking && !checked.contains(target) && !target.hasChild(checking) && !target.hasOwner(checking)){
                    checking.checkCollisionWith(target);
                }
            }
            checked.add(checking);
        }

        // check winner
        MadBalls.getGameMode().checkWinner(now);
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
        if (MadBalls.isHost()) {
            for (GameObject obj: gameObjects.values()){
                if (obj instanceof Obstacle){
                    for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                        if (player instanceof BotPlayer){
                            player.getRelevantObjIDs().add(obj.getID());
                        }
                    }
                }
            }
        }
        MadBalls.getGameMode().organize();
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
//        System.out.println("size" + gameObjects.size());
        obj.setID(id);
        gameObjects.put(id, obj);
//        System.out.println(id);
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
                obj.revive();
                deadGameObjects.remove(id);
            }
//            System.out.println("resurrect");
            return obj;
        }
    }
}
