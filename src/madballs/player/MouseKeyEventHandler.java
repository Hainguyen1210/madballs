package madballs.player;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
 
/**
 *
 * @author Paul
 */
public class MouseKeyEventHandler implements EventHandler<MouseEvent> {
     
    private final BooleanProperty isMousePressed = new SimpleBooleanProperty(false);
    private final DoubleProperty mouseX = new SimpleDoubleProperty();
    private final DoubleProperty mouseY = new SimpleDoubleProperty();
    private final MouseKeyEvent multiKeyEvent = new MouseKeyEvent();
     
    private final MouseEventHandler multiKeyEventHandler;
    
    public MouseKeyEventHandler(final MouseEventHandler handler) {
        this.multiKeyEventHandler = handler;
    }
     
    public void handle(final MouseEvent event) {
        
        if (event.getEventType() == MouseEvent.MOUSE_MOVED){
            mouseX.set(event.getSceneX());
            mouseY.set(event.getSceneY());
        }
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
            isMousePressed.set(true);
        }
        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
            isMousePressed.set(true);
            mouseX.set(event.getSceneX());
            mouseY.set(event.getSceneY());
        }
        if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            isMousePressed.set(false);
        }
        multiKeyEventHandler.handle(multiKeyEvent);
        event.consume();
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
    }
}