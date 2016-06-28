/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Caval
 */
public class Server {
    private static Service<Void> service;
    
    public static void initServer(){
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        ServerSocket listener;
                        try {
                            listener = new ServerSocket(8099);
                            try {       
                                // listen for client over socket
                                Socket socket = listener.accept();
//                                MadBalls.socket = socket;
//                                MadBalls.out = new ObjectOutputStream(socket.getOutputStream());
//                                MadBalls.in = new ObjectInputStream(socket.getInputStream());
                                    
                                // maintain the socket connection
                                while(true){
                                    try {
                                        
                                    }
                                    finally {
                                        
                                    }
                                }
                            } finally {
                                listener.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        };
        service.start();
    }
}