package madballs.gameMode;

import madballs.player.Player;

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

    public abstract void organize(); // setup how the game starts
    public abstract void manage(long now); // rule during the game
    public abstract void checkWinner(long now); // how the game ends
    public abstract void updateKill(Player killer, Player victim);

    public static GameMode getGameMode(int mode){
        switch (mode) {
            case 1:
                return new RespawnMode(0, 3);
            case 2:
                return new FlagMode(0, 3);
            default:
                return new NormalMode(0);
        }
    }
}
