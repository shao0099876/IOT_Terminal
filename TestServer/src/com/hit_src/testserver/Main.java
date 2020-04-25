package com.hit_src.testserver;

import com.hit_src.testserver.packagemanager.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static ModbusSocket modbusSocket=new ModbusSocket();
    public static PackageSocket packageSocket=new PackageSocket();
    public static String[] sensorType=new String[]{"超声测距传感器"};
    public static void main(String[] args) throws IOException {

        Thread modbusServerThread=new Thread(() -> {
            try{
                ServerSocket serverSocket=new ServerSocket(2020);
                Log.d("Modbus Listening...");
                while(true){
                    Socket socket=serverSocket.accept();
                    Log.d("Modbus Connection Established");
                    Main.modbusSocket=new ModbusSocket(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        modbusServerThread.start();

        Thread packageServerThread=new Thread(() -> {
            try{
                ServerSocket serverSocket=new ServerSocket(2021);
                Log.d("Package Listening...");
                while(true){
                    Socket socket=serverSocket.accept();
                    Log.d("Package Connection Established");
                    Main.packageSocket=new PackageSocket(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        packageServerThread.start();

        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String cmd=reader.readLine();
            if(cmd.equals("stop")){
                Log.d("STOPPING...");
                modbusServerThread.interrupt();
                modbusSocket.stop();
                packageServerThread.interrupt();
                packageSocket.stop();
                break;
            } else if(cmd.equals("readDeviceCnt")){
                modbusSocket.readDeviceCnt();
            } else if(cmd.equals("readDeviceInfo")){
                System.out.println("设备起始地址：");
                int start= Integer.parseInt(reader.readLine());
                System.out.println("设备数量：");
                int len= Integer.parseInt(reader.readLine());
                modbusSocket.readDeviceInfo(start,len);
            } else if(cmd.equals("readDeviceDataCnt")){
                modbusSocket.readDeviceDataCnt();
            } else if(cmd.equals("readDeviceData")){
                System.out.println("数据条数：");
                int len= Integer.parseInt(reader.readLine());
                modbusSocket.readDeviceData(len);
            }
            else{
                System.out.println("Unknown Command!");
            }
        }
        reader.close();
    }
}
