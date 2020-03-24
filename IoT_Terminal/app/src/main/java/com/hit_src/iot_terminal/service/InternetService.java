package com.hit_src.iot_terminal.service;

import android.os.RemoteException;

import com.hit_src.iot_terminal.protocol.Modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

public class InternetService extends AbstractRunningService {
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            Socket socket=new Socket();
            while(true){
                String hostname = null;
                int port = -1;
                try {
                    hostname=settingsService.getUpperServerAddr();
                    port=settingsService.getUpperServerPort();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    socket.connect(new InetSocketAddress(hostname,port),5000);
                } catch (IOException e) {//无法上联到服务器
                    try {
                        statusService.setInternetConnectionStatus(false);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    continue;
                }
                try {
                    statusService.setInternetConnectionStatus(true);
                    statusService.setInternetConnectionLasttime(new Date().getTime());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream=socket.getOutputStream();
                    while(true){
                        byte[] cmd=new byte[8];
                        inputStream.read(cmd);
                        byte[] rep= Modbus.exec(cmd);
                        outputStream.write(rep);
                        outputStream.flush();
                        statusService.setInternetConnectionLasttime(new Date().getTime());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }
    });

    @Override
    protected void runOnReady() {
        mainThread.start();
    }
}
