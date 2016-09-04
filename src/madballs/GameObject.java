/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import javafx.beans.binding.Bindings;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import madballs.AI.BotPlayer;
import madballs.collision.CollisionPassiveBehaviour;
import madballs.collision.CollisionEffect;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import madballs.gameFX.SoundStudio;
import madballs.moveBehaviour.MoveBehaviour;
import madballs.player.Player;
import madballs.projectiles.Projectile;

/**
 *
 * @author Caval
 */
public abstract class GameObject {
    private Integer ID;
    private GameObject owner, child;
    private Shape hitBox;
    private ImageView imageView = new ImageView();
    private String imageName;
    private Group display, animationG, statusG;
    private String dieSoundFX;
    private Rectangle boundsRectangle;
    private CollisionEffect collisionEffect;
    private CollisionPassiveBehaviour collisionPassiveBehaviour;
    
    private DoubleProperty translateX = new SimpleDoubleProperty(100);
    private DoubleProperty translateY = new SimpleDoubleProperty(100);
    private double distanceToOwner = 0;
    private Rotate rotation;
    private double oldX, oldY;
    private double oldDirection;
    private DoubleProperty hp = new SimpleDoubleProperty(100);
    private double maxHp = 100;
    private boolean isMobile = true;
    private boolean isDead = false;
    
    private StateLoader stateLoader;
    private MoveBehaviour moveBehaviour;
    private Environment environment;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public StateLoader getStateLoader(){
        return stateLoader;
    }
    
    public boolean isDead(){
        return isDead;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public void setDieSoundFX(String dieSoundFX) {
        this.dieSoundFX = dieSoundFX;
    }
    //    public int getIndex(){
//        return environment.getObjectIndex(this);
//    }
    
    public GameObject(Environment environment, double x, double y, boolean isSettingDisplay, Integer id){
//        System.out.println("1" + this.getClass());
        stateLoader = new StateLoader(this);
        translateX.set(x);
        translateY.set(y);
        oldX = x;
        oldY = y;
        rotation = new Rotate(0);
        oldDirection = 0;

        this.environment = environment;

        if (isSettingDisplay) setDisplay(id);
    }
    
    /**
     * create a new GameObject inside a created Obj
     * @param owner 
     * @param x the varied X coordinate compared to the owner (child's X = owner's X + x)
     * @param y the varied Y coordinate compared to the owner (child's Y = owner's Y + y)
     */
    public GameObject(GameObject owner, double x, double y, boolean isSettingDisplay, Integer id){
//        System.out.println("2" + this.getClass());
        stateLoader = new StateLoader(this);
//        owner.child = this;
//        this.owner = owner;
        
        distanceToOwner = Math.sqrt(x * x + y * y);

        rotation = new Rotate(0);

        setOwner(owner, x, y);
        
        if (isSettingDisplay) setDisplay(id);
    }
    
    public void setOwner(GameObject newOwner, double x, double y){
        if (owner != null){
            owner.child = null;
        }
        if (newOwner != null){
            newOwner.child = this;
            owner = newOwner;

            bindDisplay(owner, x, y);

            environment = owner.getEnvironment();
        }
        else {
            owner = null;
            translateX.unbind();
            translateY.unbind();
            rotation.angleProperty().unbind();
            setTranslateX(x);
            setTranslateY(y);
            rotation.setAngle(0);
        }
    }

    public void bindDisplay(GameObject obj, double x, double y){
        if (translateX. isBound()) translateX.unbind();
        if (translateY.isBound()) translateY.unbind();
        if (rotation.angleProperty().isBound()) rotation.angleProperty().unbind();

        translateX.bind(Bindings.add(x, obj.translateX));
        translateY.bind(Bindings.add(y, obj.translateY));

//        rotation = new Rotate(0 , -x , -y);
        rotation.setPivotX(-x);
        rotation.setPivotY(-y);
        rotation.angleProperty().bind(obj.rotation.angleProperty());
    }

    public Environment getEnvironment() {
        return environment;
    }
    
    public DoubleProperty getTranslateXProperty(){
        return translateX;
    }
    
    public DoubleProperty getTranslateYProperty(){
        return translateY;
    }

    public double getTranslateX() {
        return translateX.get();
    }

    public double getTranslateY() {
        return translateY.get();
    }
    
    public double getOwnerTranslateX() {
        if (owner != null) {
            return owner.getOwnerTranslateX();
        }
        else {
            return getTranslateX();
        }
    }

    public double getOwnerTranslateY() {
        if (owner != null) {
            return owner.getOwnerTranslateY();
        }
        else {
            return getTranslateY();
        }
    }
    
    public double getOwnerDiffX(){
        if (owner != null) {
            return getRealCoordinate()[0] - owner.getTranslateX();
        }
        else {
            return 0;
        }
    }
    
    public double getOwnerDiffY(){
        if (owner != null) {
            return getRealCoordinate()[1] - owner.getTranslateY();
        }
        else {
            return 0;
        }
    }
    
    public double[] getRealCoordinate(){
        double rotateDirection = Math.toRadians(getRotateAngle());
        double xFromPivot = -getRotate().getPivotX();
        double yFromPivot = -getRotate().getPivotY();
        double realX = getOwnerTranslateX() + yFromPivot * Math.cos(rotateDirection) - xFromPivot * Math.sin(rotateDirection);
        double realY = getOwnerTranslateY() + yFromPivot * Math.sin(rotateDirection) + xFromPivot * Math.cos(rotateDirection);
        return new double[] {realX, realY};
    }
    
    public double getDistanceToOwner(){
        return distanceToOwner;
    }
    
    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }
    
    public void setOwnerOldY(double oldY) {
        if (owner != null){
            owner.setOwnerOldY(oldY);
        }
        setOldY(oldY);
    }
    
    public double getOwnerOldY(){
        if (owner != null){
            return owner.getOwnerOldY();
        }
        return oldY;
    }
    
    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }
    
    public void setOwnerOldX(double oldX) {
        if (owner != null){
            owner.setOwnerOldX(oldX);
        }
        setOldX(oldX);
    }
    
    public double getOwnerOldX(){
        if (owner != null){
            return owner.getOwnerOldX();
        }
        return oldX;
    }
    
    public Shape getHitBox(){
        return hitBox;
    }
    
    public ImageView getImageView() {
        return imageView;
    }


    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }

    public Group getDisplay() {
        return display;
    }

    public Group getAnimationG() {
      return animationG;
    }

    public Group getStatusG() {
      return statusG;
    }
    
    public void setRotate(double direction){
        double angle = Math.toDegrees(direction);
        if (owner != null) {
            owner.rotation.setAngle(angle);
        }
        else {
            rotation.setAngle(angle);
        }
    }
    
    public double getRotateAngle(){
        return rotation.getAngle();
    }
    
    public Rotate getRotate(){
        return rotation;
    }
    
    public double getOldDirection(){
        if (owner != null){
            return owner.getOldDirection();
        }
        return oldDirection;
    }
    
    public void setOldDirection(double oldDirection) {
        if (owner != null){
            owner.setOldDirection(oldDirection);
        }
        this.oldDirection = oldDirection;
    }
    
    public CollisionEffect getCollisionEffect(){
        return collisionEffect;
    }

    public CollisionPassiveBehaviour getCollisionPassiveBehaviour() {
        return collisionPassiveBehaviour;
    }

    public MoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }

    public double getHpValue() {
        return hp.get();
    }

    public void setHitBox(Shape hitBox) {
        this.hitBox = hitBox;
    }

    public void setImage(String imageName) {
        this.imageName = imageName;
        this.imageView.setImage(ImageGenerator.getInstance().getImage(imageName));
    }

    public String getImageName(){
        return imageName;
    }

    public DoubleProperty getHp(){
        return hp;
    }

    public void setHpValue(double hp) {
        if (hp > maxHp) hp = maxHp;
        this.hp.set(hp);
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
        if (getHpValue() > maxHp) setHpValue(maxHp);
    }

    public void setTranslateX(double newX) {
        if (owner != null) {
            owner.setTranslateX(newX);
        }
        else {
            translateX.set(newX);
        }
    }

    public void setTranslateY(double newY) {
        if (owner != null) {
            owner.setTranslateY(newY);
        }
        else {
            translateY.set(newY);
        }
    }

    public void setMoveBehaviour(MoveBehaviour moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }

    final public void setCollisionEffect(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    final public void setCollisionPassiveBehaviour(CollisionPassiveBehaviour collisionPassiveBehaviour) {
        this.collisionPassiveBehaviour = collisionPassiveBehaviour;
    }

    public GameObject getOwner() {
        return owner;
    }

    public GameObject getChild() {
        return child;
    }
    
    /**
     * check whether this obj has the specific owner
     * @param owner
     * @return 
     */
    public boolean hasOwner(GameObject owner){
        if (this.owner == null) return false;
        if (this.owner == owner) return true;
        return this.owner.hasOwner(owner);
    }
    
    /**
     * check whether this obj has the specific child
     * @param child
     * @return 
     */
    public boolean hasChild(GameObject child){
        if (this.child == null) return false;
        if (this.child == child) return true;
        return this.child.hasChild(child);
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and return boolean
     * @param target
     * @return 
     */
    public Shape getCollisionShapeWith(GameObject target){
       Shape intersect = Shape.intersect(hitBox, target.getHitBox());
       return intersect;
    }
    
    /**
     * check whether this Collidable has collided with the target Collidable
     * and act accordingly
     * @param target 
     */
    public boolean checkCollisionWith(GameObject target){
        Shape collisionShape = getCollisionShapeWith(target);
        if (collisionShape.getBoundsInLocal().getWidth() != -1){
//            System.out.println("collide");
            // let the collision effects affect the two collided objects
//            if(this instanceof Item && target instanceof Obstacle){  System.out.println(" Item checked Obstacle");  }
            onCollision(target, collisionShape);
            target.onCollision(this, collisionShape);
//            System.out.println("collide ");
//            System.out.println(target.getTranslateX());
//            System.out.println(target.getTranslateY());
//            System.out.println(target.getDisplay().getBoundsInParent().getWidth());
//            System.out.println(target.getDisplay().getBoundsInParent().getHeight());
            return true;
        }
        return false;
    }
    
    /**
     * the action the obj makes upon collision
     * @param target
     * @param collisionShape 
     */
    public void onCollision(GameObject target, Shape collisionShape){
        collisionEffect.affect(this, target, collisionShape);
    }
    
    /**
     * put all the display component inside the display HBox
     */
    public void setDisplay(Integer id){
        display = new Group();
        animationG = new Group();
        statusG = new Group();
        statusG.setVisible(false);
        statusG.setTranslateZ(-1);
        statusG.setTranslateX(-20);
        statusG.setTranslateY(20);
//        display.setPrefSize(0, 0);
        display.translateXProperty().bind(translateX);
        display.translateYProperty().bind(translateY);
        animationG.getTransforms().add(rotation);
//        for (Node child : display.getChildren()){
//            child.translateXProperty().bind(translateX);
//            child.translateYProperty().bind(translateY);
//            child.getTransforms().add(rotation);
//        }
        setDisplayComponents();
        imageView.setSmooth(true); imageView.setCache(true); //try these
        animationG.getChildren().addAll(hitBox, imageView);
        display.getChildren().addAll(animationG, statusG);
        getDisplay().setOnMouseEntered(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            statusG.setVisible(true);
          }
        });
        getDisplay().setOnMouseExited(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            statusG.setVisible(false);
          }
        });
//        hitBox.setVisible(false);
        hitBox.setOpacity(0);
        hitBox.setCache(true);
        hitBox.setCacheHint(CacheHint.SPEED);
        environment.registerGameObj(this, true, id);
    }

    public void configImageView(double relativeX, double relativeY, double height, double width){
        this.imageView.setTranslateX(relativeX);
        this.imageView.setTranslateY(relativeY);
        this.imageView.setFitHeight(height);
        this.imageView.setFitWidth(width);
    }


    /**
     * update the rectangle shape representing the bounds of the game obj
     */
    public void updateBoundsRectangle(){
        Bounds bounds = display.getBoundsInParent();
        boundsRectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }
    
    public Rectangle getBoundsRectangle(){
        return boundsRectangle;
    }

    public void revive(){
        if (isDead){
            isDead = false;
            if (!environment.getDisplay().getChildren().contains(display)){
                environment.getDisplay().getChildren().add(display);
            }
        }
    }
    
    public void setDead(){
//        System.out.println("remove " + getClass() + getID());
        if (dieSoundFX != null) {
            SoundStudio.getInstance().playAudio(dieSoundFX, getTranslateX(), getTranslateY(), 500, 500);
        }
        isDead = true;
//        if (owner != null) {
//            owner.child = null;
//        }
        getEnvironment().removeGameObj(this);
    }
    
    public void die(){
        if (this instanceof Ball && !MadBalls.isHost()) return;
        setDead();
        if (child != null && !child.isDead()) {
            child.die();
        }
//        if (MadBalls.isHost()){
////            System.out.println("die " + getClass() + getID());
//            setDead();
//            if (child != null) {
//                child.die();
//            }
//        }
//        else {
//            getDisplay().setVisible(false);
//        }
    }

    public void dieWithOwner(){
        die();
        if (owner != null){
            owner.dieWithOwner();
        }
    }
    
    public void update(long now){
        if (!isDead) {
            if (moveBehaviour != null) moveBehaviour.move(now);
            updateUnique(now);
        }
        if (MadBalls.isHost()) {
            if (!(this instanceof Obstacle)) {
                updateBotRelevancy();
            }
        }
//        else {
//            MadBalls.getMultiplayerHandler().getLocalPlayer().checkRelevancy(this, 500, 500);
//        }
        stateLoader.update(now);
    }

    private void updateRelevancy(){
        for (Player player : MadBalls.getMultiplayerHandler().getPlayers()){
            player.checkRelevancy(this, 500, 500);
        }
    }

    private void updateBotRelevancy(){
        for (BotPlayer bot : BotPlayer.getBotPlayers()){
            bot.checkRelevancy(this, 0, 0);
        }
    }
    
    /**
     * the obj must implement this method to set its hit box and image, etc.
     */
    public abstract void setDisplayComponents();
    
    /**
     * the obj must implement this method to update with the environment's animation timer
     * @param now 
     */
    public abstract void updateUnique(long now);
}
