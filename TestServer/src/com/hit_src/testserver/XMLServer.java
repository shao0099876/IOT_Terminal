package com.hit_src.testserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class XMLServer {
    private ServerSocket serverSocket;
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    Socket socket=serverSocket.accept();
                    Log.d("Connection established");
                    Main.xmlSocket=new XMLSocket(socket);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    });
    public XMLServer() throws IOException {
        serverSocket=new ServerSocket(2021);
        Log.d("XMLListening...");
        mainThread.start();
    }
    public void stop() throws IOException {
        mainThread.interrupt();
        serverSocket.close();
    }
}
