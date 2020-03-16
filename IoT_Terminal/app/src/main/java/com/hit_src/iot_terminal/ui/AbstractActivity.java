package com.hit_src.iot_terminal.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.IStatusService;

public abstract class AbstractActivity extends AppCompatActivity {

    protected abstract void runOnBindService();

    protected IStatusService statusService;
    protected IDatabaseService dbService;
    private int serviceReadyCNT=0;
    protected ServiceConnection statusServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            statusService=IStatusService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    runOnBindService();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    protected ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    runOnBindService();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(statusServiceConnection);
        unbindService(dbServiceConnection);
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

}
