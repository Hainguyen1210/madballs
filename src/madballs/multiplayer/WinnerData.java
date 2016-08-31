package madballs.multiplayer;

/**
 * Created by caval on 26/08/2016.
 */
public class WinnerData extends Data {
    private int winnerTeamNum;

    public int getWinnerTeamNum() {
        return winnerTeamNum;
    }

    public WinnerData(int winnerTeamNum) {
        super("winner");
        this.winnerTeamNum = winnerTeamNum;
    }
}
