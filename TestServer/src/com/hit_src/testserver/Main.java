package com.hit_src.testserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static ModbusSocket modbusSocket=new ModbusSocket();
    public static XMLSocket xmlSocket=new XMLSocket();
    public static void main(String[] args) throws IOException {
        ModbusServer modbusServer=new ModbusServer();
        XMLServer xmlServer=new XMLServer();
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String cmd=reader.readLine();
            if(cmd.equals("stop")){
                Log.d("STOPPING...");
                modbusServer.stop();
                modbusSocket.stop();
                xmlServer.stop();
                xmlSocket.stop();
                break;
            } else if(cmd.equals("readDevNum")){

            } else{
                System.out.println("Unknown Command!");
            }
        }
        reader.close();
        return;
    }
}
