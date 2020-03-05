package com.hit_src.iot_terminal.service.internet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hit_src.iot_terminal.internet.UpperServer;

import java.util.Timer;
import java.util.TimerTask;

public class InternetService extends Service {

    @Override
    public void onCreate(){//初始化
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){//从这里启动处理线程
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpperServer.heartBeat();
            }
        },0,3000);
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
