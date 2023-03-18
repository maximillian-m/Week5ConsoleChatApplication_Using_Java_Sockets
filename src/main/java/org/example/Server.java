package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    //Creating a startServer method that has a new thread for every instance of clientHandler;
    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){
            closeServerSocket();
        }
    }

    //creating a closeServerSocket method that closes server-socket and used in the startServer to avoid nested try and catch
    public void closeServerSocket(){
        if(serverSocket !=  null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(1888);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
