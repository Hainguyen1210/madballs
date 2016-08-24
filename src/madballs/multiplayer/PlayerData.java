package madballs.multiplayer;

import madballs.player.Player;

/**
 * Created by caval on 24/08/2016.
 */
public class PlayerData extends Data {
    private String name;
    private int number;
    private int teamNumber;
    private boolean isLocal;

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public PlayerData(Player player, boolean isLocal) {
        super("player");
        this.name = player.getName();
        this.number = player.getPlayerNum();
        this.teamNumber = player.getTeamNum();
        this.isLocal = isLocal;
    }

    public PlayerData(Player player){
        super("update_player");
        this.number = player.getPlayerNum();
        this.teamNumber = player.getTeamNum();
    }
}
