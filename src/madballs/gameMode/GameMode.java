package madballs.gameMode;

/**
 * Created by caval on 31/08/2016.
 */
public abstract class GameMode {
    private int mode;

    public GameMode(int mode){
        this.mode = mode;
    }

    public abstract void organize(); // setup how the game starts
    public abstract void manage(long now); // rule during the game
    public abstract void checkWinner(long now); // how the game ends
}
