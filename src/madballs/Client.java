package madballs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Client class handles major socket operations on the client side
 * @author Caval
 */
public class Client{
    private static Service<Void> service;
    
    /**
     * connect to the server as client via socket
     */
    public static void initClient(){
        // get the server ip and port
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            // connect socket
                            Socket socket = new Socket("127.0.0.1", 8099);
                            MadBalls.socket = socket;
                                MadBalls.out = new ObjectOutputStream(socket.getOutputStream());
                                MadBalls.in = new ObjectInputStream(socket.getInputStream());
                                    
                                // maintain the socket connection
                                while(true){
                                    try {
                                        String input = (String)MadBalls.in.readObject();
                                        String[] inputs = input.split(" ");
                                        if (inputs[0].equals("x")){
                                            if (inputs[1].equals("0")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityX(0);
                                            }
                                            else if (inputs[1].equals("-")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityX(-100);
                                            }
                                            else if (inputs[1].equals("+")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityX(100);
                                            }
                                        }
                                        else if (inputs[0].equals("y")){
                                            if (inputs[1].equals("0")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityY(0);
                                            }
                                            else if (inputs[1].equals("-")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityY(-100);
                                            }
                                            else if (inputs[1].equals("+")){
                                                MadBalls.nextPlayer.ball.getMoveBehaviour().setVelocityY(100);
                                            }
                                        }
                                    }
                                    finally {
                                        
                                    }
                                }
                        }
                        catch (IOException ex){
                            // retry if could not connect to server
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    initClient();
                                }
                            });
                        }
                        return null;
                    }
                };
            }
        };
        service.start();
    }

}
