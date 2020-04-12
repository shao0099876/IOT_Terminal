package com.hit_src.iot_terminal;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.Datatype;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.ISettingsService;
import com.hit_src.iot_terminal.tools.Filesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainApplication extends Application {
    static {
        System.loadLibrary("JNISO");
    }

    boolean runSerialService=true;
    boolean runInternetService=true;

    public static IDatabaseService dbService;
    public static ISettingsService settingsService;
    private static volatile int serviceReadyCnt=0;
    protected ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCnt){
                serviceReadyCnt+=1;
                if(serviceReadyCnt>=2){
                    if(runSerialService){
                        startService(new Intent().setAction("SensorService"));
                    }
                    if(runInternetService){
                        startService(new Intent().setAction("InternetService"));
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    protected ServiceConnection settingServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            settingsService=ISettingsService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCnt){
                serviceReadyCnt+=1;
                if(serviceReadyCnt>=2){
                    if(runSerialService){
                        startService(new Intent().setAction("SensorService"));
                    }
                    if(runInternetService){
                        startService(new Intent().setAction("InternetService"));
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public static HashMap<Integer, SensorType> sensorTypeHashMap=new HashMap<>();
    public static HashMap<String, Integer> xmlRecordHashMap=new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.ISettingsService"),settingServiceConnection,BIND_AUTO_CREATE);
        Filesystem.build(this);
        WifiManager manager= (WifiManager) getSystemService(WIFI_SERVICE);
        if(!manager.isWifiEnabled()){
            manager.setWifiEnabled(true);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent().setAction("SerialService"));
        stopService(new Intent().setAction("InternetService"));
    }
}
