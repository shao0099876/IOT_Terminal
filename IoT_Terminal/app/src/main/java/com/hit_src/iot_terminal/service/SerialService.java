package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.protocol.Modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SerialService extends Service {
    private IStatusService statusService;
    private IDatabaseService dbService;
    private int serviceReadyCNT=0;
    private ServiceConnection statusServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            statusService=IStatusService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    mainThread.start();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    mainThread.start();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                ArrayList<Sensor> sensorList=null;
                try {
                    sensorList= (ArrayList<Sensor>) dbService.getSensorList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                for(Sensor i:sensorList){
                    send(i);
                    recv(i);
                    try {
                        statusService.setSensorStatus(i.ID,true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    private void send(Sensor i){
        byte[] cmd=i.packageCMD();
        SerialPort.write(cmd);
    }
    private void recv(Sensor i){
        byte[] raw_data=SerialPort.read(i.replyLength);
        int res=i.unpackage(raw_data);
        try {
            dbService.addSensorData(i.ID,res);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(){//初始化
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){//从这里启动处理线程
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
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
