package com.hit_src.iot_terminal;

import android.app.Application;
import android.content.Intent;

public class MainApplication extends Application {
    static {
        System.loadLibrary("serial-HAI");
        System.loadLibrary("network-HAI");
        System.loadLibrary("Global-JNI");
    }
    boolean runSerialService=false;
    boolean runInternetService=false;

    @Override
    public void onCreate() {
        super.onCreate();
        if(runSerialService){
            startService(new Intent().setAction("SerialService"));
        }
        if(runInternetService){
            startService(new Intent().setAction("InternetService"));
        }
    }
}
