package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit_src.iot_terminal.ui.OverviewActivity;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("serial-HAI");
        System.loadLibrary("network-HAI");
        System.loadLibrary("Global-JNI");
    }
    boolean runOverview=true;
    boolean runSerialService=true;
    boolean runInternetService=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(runSerialService){
            startService(new Intent().setAction("SerialService"));
        }
        if(runInternetService){
            startService(new Intent().setAction("InternetService"));
        }
        if(runOverview){
            Intent intent=new Intent(this, OverviewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
