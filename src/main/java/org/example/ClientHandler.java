package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
private Socket socket;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;
private String clientUsername;

public ClientHandler(Socket socket){
    try {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientUsername = bufferedReader.readLine();
        clientHandlers.add(this);
        broadCastMessage("SERVER: " + clientUsername + " has joined the chat!");
    }catch(IOException e){
        closeEverything(socket, bufferedWriter, bufferedReader);
    }
}
    @Override
    public void run() {
    String messageFromClient;

    while(socket.isConnected()){
        try{
            messageFromClient = bufferedReader.readLine();
            if(messageFromClient == null){
                removeClientHandler();
                break;
            }
            broadCastMessage(messageFromClient);
        }catch(IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
            break;
        }
    }
    }
    public void broadCastMessage(String messageSend){
    for(ClientHandler clientHandler : clientHandlers){
        try{
            if(!clientHandler.clientUsername.equals(clientUsername)){
                clientHandler.bufferedWriter.write(messageSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
        }catch(IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }
    }
    public void removeClientHandler(){
    clientHandlers.remove(this);
    if(clientUsername != null){
    broadCastMessage("SERVER: " + clientUsername + " has left the chat");
    }
}
    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
    try{
        removeClientHandler();
        if(socket != null){
            socket.close();
        }
        if(bufferedWriter != null){
            bufferedWriter.close();
        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
    }catch (IOException e) {
        e.printStackTrace();
        }
    }
}
