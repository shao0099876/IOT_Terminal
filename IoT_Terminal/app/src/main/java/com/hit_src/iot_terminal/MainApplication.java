package com.hit_src.iot_terminal;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.ISettingsService;
import com.hit_src.iot_terminal.tools.Filesystem;

import java.util.HashMap;

public class MainApplication extends Application {
    public static MainApplication self;
    public static ISettingsService settingsService;
    public static HashMap<Integer, SensorType> sensorTypeHashMap = new HashMap<>();
    public static HashMap<String, Integer> xmlRecordHashMap = new HashMap<>();
    private static volatile int serviceReadyCnt = 0;

    static {
        System.loadLibrary("JNISO");
    }

    boolean runSerialService = true;
    boolean runInternetService = true;
    protected ServiceConnection settingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            settingsService = ISettingsService.Stub.asInterface(service);
            synchronized ((Integer) serviceReadyCnt) {
                serviceReadyCnt += 1;
                if (serviceReadyCnt >= 2) {
                    if (runSerialService) {
                        startService(new Intent().setAction("SensorService"));
                    }
                    if (runInternetService) {
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
        bindService(new Intent("com.hit_src.iot_terminal.service.ISettingsService"), settingServiceConnection, BIND_AUTO_CREATE);
        Filesystem.build(this);
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (!manager.isWifiEnabled()) {
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
