/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

/**
 * http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
 * @author Steven Lambert
 */
public class Quadtree {
 
    private int MAX_OBJECTS = 5;
    private int MAX_LEVELS = 5;

    private int level;
    private ArrayList<GameObject> objects;
    private Rectangle bounds;
    private Quadtree[] nodes;

   /*
    * Constructor
    */
    public Quadtree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
//        System.out.println("pLvl " + pLevel);
    }

    /*
    * Clears the quadtree
    */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
              nodes[i].clear();
              nodes[i] = null;
            }
        }
    }
    
    /*
    * Splits the node into 4 subnodes
    */
    private void split() {
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        nodes[0] = new Quadtree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }
    
    /*
    * Determine which node the object belongs to. -1 means
    * object cannot completely fit within a child node and is part
    * of the parent node
    */
    private int getIndex(GameObject obj) {
        Bounds objBounds = obj.getDisplay().getBoundsInParent();
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (objBounds.getMinY() < horizontalMidpoint && objBounds.getMaxY() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (objBounds.getMinY() > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (objBounds.getMinX() < verticalMidpoint && objBounds.getMaxX() < verticalMidpoint) {
            if (topQuadrant) {
              index = 1;
            }
            else if (bottomQuadrant) {
              index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        else if (objBounds.getMinX() > verticalMidpoint) {
            if (topQuadrant) {
              index = 0;
            }
            else if (bottomQuadrant) {
              index = 3;
            }
        }

        return index;
    }
    
    /*
    * Insert the object into the quadtree. If the node
    * exceeds the capacity, it will split and add all
    * objects to their corresponding nodes.
    */
    public void insert(GameObject obj) {
      if (nodes[0] != null) {
        int index = getIndex(obj);

        if (index != -1) {
          nodes[index].insert(obj);

          return;
        }
      }

      objects.add(obj);

      if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
         if (nodes[0] == null) { 
            split(); 
         }

        int i = 0;
        while (i < objects.size()) {
          int index = getIndex(objects.get(i));
          if (index != -1) {
            nodes[index].insert(objects.remove(i));
          }
          else {
            i++;
          }
        }
      }
    }
    
    /*
    * Return all objects that could collide with the given object
    */
    public List<GameObject> retrieve(List<GameObject> returnObjects, GameObject obj) {
      int index = getIndex(obj);
      if (index != -1 && nodes[0] != null) {
        nodes[index].retrieve(returnObjects, obj);
      }
      else if (index == -1 && nodes[0] != null){
        for (int i = 0; i < 4; i++){
            nodes[i].retrieve(returnObjects, obj);
        }  
      }
      returnObjects.addAll(objects);
      
//      System.out.println("x: " + bounds.getX() + "; y: " + bounds.getY() + "; w: " + bounds.getWidth() + "; h: " + bounds.getHeight());
      return returnObjects;
    }
}
