/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Caval
 */
public class Quadtree {
 
  private int MAX_OBJECTS = 10;
  private int MAX_LEVELS = 5;
 
  private int level;
  private List objects;
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
  }
}
