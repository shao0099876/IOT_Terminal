package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hit_src.iot_terminal.service.IStatusService;

public class MainActivity extends AppCompatActivity {
    private IStatusService iStatusService;
    private ServiceConnection statusServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("SRCDEBUG","sereviceConnected");
            iStatusService=IStatusService.Stub.asInterface(service);
            try {
                boolean test=iStatusService.getInternetConnectionStatus();
                Log.d("SRCDEBUG",Boolean.toString(test));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);


    }
}
