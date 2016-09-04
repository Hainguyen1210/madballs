package madballs.gameMode;

/**
 * Created by caval on 31/08/2016.
 */
public abstract class GameMode {
    private int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public abstract void organize();
    public abstract void manage(long now);
    public abstract void checkWinner(long now);
}
