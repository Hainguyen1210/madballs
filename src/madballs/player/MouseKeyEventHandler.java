package madballs.player;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import madballs.MadBalls;
import madballs.map.Map;
import madballs.scenes.SceneManager;
import madballs.multiplayer.MouseInputData;

/**
 * handle mouse
 * @author Paul
 */
public class MouseKeyEventHandler implements EventHandler<MouseEvent> {
     
    private final BooleanProperty isMousePressed = new SimpleBooleanProperty(false);
    private final DoubleProperty mouseX = new SimpleDoubleProperty();
    private final DoubleProperty mouseY = new SimpleDoubleProperty();
    private final DoubleProperty scale = new SimpleDoubleProperty();
    private final MouseKeyEvent multiKeyEvent = new MouseKeyEvent();
    private Player player;
     
    private final MouseEventHandler multiKeyEventHandler;
    
    public MouseKeyEventHandler(final MouseEventHandler handler, Player player) {
        this.multiKeyEventHandler = handler;
        this.player = player;
    }

    public void clear(){
        isMousePressed.set(false);
    }
     
    public void handle(final MouseEvent event) {
        SceneManager sceneManager = SceneManager.getInstance();
        scale.set(sceneManager.getScale());
        double targetX = event.getSceneX();
        double targetY = event.getSceneY();

        if (!MadBalls.isHost()) player.sendData(new MouseInputData(event.getEventType().getName(), targetX, targetY, scale.get()));
        if (event.getEventType() == MouseEvent.MOUSE_MOVED){
            mouseX.set(targetX);
            mouseY.set(targetY);
        }
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
            isMousePressed.set(true);
        }
        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
            isMousePressed.set(true);
            mouseX.set(targetX);
            mouseY.set(targetY);
        }
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            isMousePressed.set(false);
            if (player.getBall().isDead()){
                SceneManager.getInstance().setZoomOut(SceneManager.NUM_MAP_PARTS *1.1);
                PerspectiveCamera camera = SceneManager.getInstance().getCamera();
                Map map = player.getBall().getEnvironment().getMap();
                camera.translateXProperty().unbind();
                camera.translateYProperty().unbind();
                camera.setTranslateX(map.getWidth()/2);
                camera.setTranslateY(map.getHeight()/2);
            }
        }
        multiKeyEventHandler.handle(multiKeyEvent);
        event.consume();
    }
    
    public void handle(MouseInputData data){
        scale.set(data.getScale());
        if (data.getEventType().equals("MOUSE_MOVED")){
            mouseX.set(data.getX());
            mouseY.set(data.getY());
        }
        if (data.getEventType().equals("MOUSE_PRESSED")){
            isMousePressed.set(true);
        }
        if (data.getEventType().equals("MOUSE_DRAGGED")){
            isMousePressed.set(true);
            mouseX.set(data.getX());
            mouseY.set(data.getY());
        }
        if (data.getEventType().equals("MOUSE_RELEASED")){
            isMousePressed.set(false);
        }
//        scale.set(data.getScale());
        multiKeyEventHandler.handle(multiKeyEvent);
    }
     
    public interface MouseEventHandler {
        void handle(final MouseKeyEvent event);
    }
     
    public class MouseKeyEvent {
        public boolean isPressed() {
            return isMousePressed.get();
        }
        
        public double getMouseX(){
            return mouseX.get();
        }
        
        public double getMouseY(){
            return mouseY.get();
        }

        public double getScale() { return scale.get(); }
    }
}