package madballs.multiplayer;

/**
 * Created by caval on 26/08/2016.
 */
public class ScoreData extends Data {
    private int winnerTeamNum;
    private int value;

    public int getValue() {
        return value;
    }

    public int getWinnerTeamNum() {
        return winnerTeamNum;
    }

    public ScoreData(int winnerTeamNum, int value) {
        super("score");
        this.winnerTeamNum = winnerTeamNum;
        this.value = value;
    }
}
