package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class AbstractRunningService extends Service {
    protected IDatabaseService dbService;
    protected ISettingsService settingsService;
    private int serviceReadyCNT=0;
    protected abstract void runOnReady();
    protected ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    runOnReady();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    protected ServiceConnection settingsServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            settingsService=ISettingsService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    runOnReady();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onCreate(){//初始化
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.ISettingsService"),settingsServiceConnection,BIND_AUTO_CREATE);
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(dbServiceConnection);
        unbindService(settingsServiceConnection);
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
