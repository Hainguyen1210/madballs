package madballs.player;

import java.util.EnumSet;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import madballs.MadBalls;
import madballs.multiplayer.KeyInputData;
 
/**
 *
 * @author Paul
 */
public class MultiplePressedKeysEventHandler implements EventHandler<KeyEvent> {
     
    private final Set<KeyCode> buffer = EnumSet.noneOf(KeyCode.class);
    private final MultiKeyEvent multiKeyEvent = new MultiKeyEvent();
    private Player player;
     
    private final MultiKeyEventHandler multiKeyEventHandler;
    
    public MultiplePressedKeysEventHandler(final MultiKeyEventHandler handler, Player player) {
        this.multiKeyEventHandler = handler;
        this.player = player;
    }
     
    public void handle(final KeyEvent event) {
        if (!MadBalls.isHost()) player.sendData(new KeyInputData(event));
        final KeyCode code = event.getCode();
//        System.out.println("type" + event.getEventType());
        if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            buffer.remove(code);
        }
        else if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            buffer.add(code);
        }
        multiKeyEventHandler.handle(multiKeyEvent);
        event.consume();
    }
     
    public interface MultiKeyEventHandler {
        void handle(final MultiKeyEvent event);
    }
     
    public class MultiKeyEvent {
        public boolean isPressed(final KeyCode key) {
            return buffer.contains(key);
        }
        public boolean isKeyFree(){
            return buffer.size() == 0;
        }
    }
}