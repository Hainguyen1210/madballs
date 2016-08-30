package madballs.AI;

import javafx.application.Platform;
import madballs.MadBalls;
import madballs.multiplayer.Client;
import madballs.multiplayer.Data;
import madballs.multiplayer.MultiplayerHandler;
import madballs.multiplayer.ReadyData;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by caval on 27/08/2016.
 */
public class BotClient extends MultiplayerHandler {
    public BotClient() {
        super(false);
    }

    @Override
    public void sendData(Data data) {
        getLocalPlayer().sendData(data);
    }

    @Override
    public void handleData(Data data){
        super.handleData(data);
        try {
            if (data.getType().equals("check_ready")){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        sendData(new ReadyData(MadBalls.getAnimationScene().getWidth(), MadBalls.getAnimationScene().getHeight()));
                    }
                });
            }
        }
        catch (Exception ex){
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
            System.exit(0);
        }

    }


    @Override
    public void init() {

    }
}
