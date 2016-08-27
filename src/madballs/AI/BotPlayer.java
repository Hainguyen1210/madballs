package madballs.AI;

import madballs.MadBalls;
import madballs.multiplayer.Client;
import madballs.multiplayer.Data;
import madballs.player.Player;

/**
 * Created by caval on 27/08/2016.
 */
public class BotPlayer extends Player {
    private BotClient botClient = new BotClient();

    public BotClient getBotClient() {
        return botClient;
    }

    public BotPlayer() {
        super(null, false);
        botClient.setLocalPlayer(this);
    }

    @Override
    public void sendData(Data data){
        System.out.println("bot send" + data.getType());
        MadBalls.getMultiplayerHandler().handleData(data);
    }
}
