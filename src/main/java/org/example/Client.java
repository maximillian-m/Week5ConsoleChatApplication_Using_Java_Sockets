package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
private Socket socket;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;
private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void listenToMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;

                while(socket.isConnected()){
                    try{
                        messageFromChat = bufferedReader.readLine();
                        System.out.println(messageFromChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if (socket != null) {
                socket.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your userName for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1888);
        Client client = new Client(socket, username);
        client.listenToMessage();
        client.sendMessage();
    }
}
