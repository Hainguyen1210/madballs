package madballs.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import madballs.player.Player;

/**
 * Client class handles major socket operations on the client side
 * @author Caval
 */
public class Client extends MultiplayerHandler{
    
    /**
     * connect to the server as client via socket
     */
    public void init(){
        // get the server ip and port
        setService(new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            // connect socket
//                            System.out.println("why");
                            Socket socket = new Socket("127.0.0.1", 8099);
                            setLocalPlayer(new Player(socket, false));
                                    
                                // maintain the socket connection
                                while(true){
                                    try {
                                        System.out.println("handle");
                                        handleData(getLocalPlayer().readData());
                                    }
                                    finally {
                                        
                                    }
                                }
                        }
                        catch (IOException ex){
                            // retry if could not connect to server
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        });
        getService().start();
    }

}
