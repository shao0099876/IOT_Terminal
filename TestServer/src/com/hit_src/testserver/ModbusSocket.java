package com.hit_src.testserver;

import java.io.IOException;
import java.net.Socket;

public class ModbusSocket {
    private Socket mainSocket=null;
    public ModbusSocket(Socket socket){
        mainSocket=socket;
    }
    public ModbusSocket(){
    }

    public void stop() throws IOException {
        if(mainSocket!=null){
            mainSocket.close();
        }
    }
}
