package com.hit_src.testserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ModbusServer {
    private ServerSocket serverSocket;
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    Socket socket=serverSocket.accept();
                    Log.d("Connection established!");
                    Main.modbusSocket=new ModbusSocket(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    public ModbusServer() throws IOException {
        serverSocket=new ServerSocket(2020);
        Log.d("Listening...");
        mainThread.start();
    }
    public void stop() throws IOException {
        mainThread.interrupt();
        serverSocket.close();
    }
}
