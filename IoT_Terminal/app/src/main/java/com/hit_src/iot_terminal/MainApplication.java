package com.hit_src.iot_terminal;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.ISettingsService;
import com.hit_src.iot_terminal.tools.pm.PackageManager;


public class MainApplication extends Application {
    static {
        System.loadLibrary("JNISO");
    }
    public static MainApplication self;

    private final boolean runSerialService=true;
    private final boolean runInternetService=true;

    public static IDatabaseService dbService;
    public static ISettingsService settingsService;
    private static volatile int serviceReadyCnt=0;
    private final ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCnt){
                serviceReadyCnt+=1;
                if(serviceReadyCnt>=2){
                    init();
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
    private final ServiceConnection settingServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            settingsService=ISettingsService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCnt){
                serviceReadyCnt+=1;
                if(serviceReadyCnt>=2){
                    init();
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
    @Override
    public void onCreate() {
        super.onCreate();
        self=this;
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.ISettingsService"),settingServiceConnection,BIND_AUTO_CREATE);

    }
    private void init(){
        PackageManager.testPackageListDir();
        GlobalVar.createInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent().setAction("SerialService"));
        stopService(new Intent().setAction("InternetService"));
    }
}
