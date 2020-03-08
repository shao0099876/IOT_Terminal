package com.hit_src.iot_terminal.service.internet;

import android.app.Service;
import android.content.Intent;
import android.icu.util.Output;
import android.os.IBinder;
import android.util.Log;

import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.profile.status.Status;
import com.hit_src.iot_terminal.protocol.Modbus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class InternetService extends Service {

    @Override
    public void onCreate(){//初始化
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){//从这里启动处理线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String addr= InternetSettings.getServer();
                int port=InternetSettings.getServerPort();
                Socket socket=null;
                InputStream inputStream=null;
                OutputStream outputStream=null;
                while(true){
                    try {
                        socket=new Socket();
                        SocketAddress socketAddress=new InetSocketAddress(addr,port);
                        socket.connect(socketAddress,1000);
                        socket.setSoTimeout(3000);
                        inputStream=socket.getInputStream();
                        outputStream=socket.getOutputStream();
                        byte[] cmd=new byte[8];
                        while(true){
                            inputStream.read(cmd,0,8);
                            outputStream.write(Modbus.exec(cmd));
                            outputStream.flush();
                        }
                    } catch (IOException e) {
                        Log.d("Internet","Unable to access server");
                        Log.d("Internet",e.toString());
                    }
                    Status.setStatusData(Status.INTERNET_CONNECTION_STATUS,false);
                }
            }
        }).start();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
