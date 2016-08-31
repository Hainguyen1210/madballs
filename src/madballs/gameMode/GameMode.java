package madballs.gameMode;

/**
 * Created by caval on 31/08/2016.
 */
public abstract class GameMode {
    private int mode;

    public GameMode(int mode){
        this.mode = mode;
    }

    public abstract void organize();
}
