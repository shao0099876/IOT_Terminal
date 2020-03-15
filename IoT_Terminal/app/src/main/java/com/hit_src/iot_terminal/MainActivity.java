package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.service.IStatusService;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("serial-HAI");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        byte[] test=new byte[]{0,0,1,0x55};
        SerialPort.write(test);
        byte[] res=SerialPort.read(2);
        for(int i=0;i<res.length;i++){
            Log.d("SRCDEBUG", Byte.toString(res[i]));
        }
    }
}
