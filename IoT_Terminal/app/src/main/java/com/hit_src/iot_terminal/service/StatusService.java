package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;

public class StatusService extends Service {

    private static volatile ArrayList<Integer> sensorSet=new ArrayList<>();
    private static volatile boolean internetConnection=false;
    private static volatile long internetConnectionLasttime=0;

    public class StatusServiceImpl extends IStatusService.Stub{
        @Override
        public ArrayList<Integer> getSensorList() throws RemoteException {
            return sensorSet;
        }

        @Override
        public boolean getInternetConnectionStatus(){
            return internetConnection;
        }

        @Override
        public long getInternetConnectionLasttime() throws RemoteException {
            return internetConnectionLasttime;
        }
        @Override
        public void setSensorStatus(int target, boolean status){
            if(status){
                if(sensorSet.contains(target)){
                    //do nothing
                }
                else{
                    sensorSet.add(target);
                }
            } else{
                if(sensorSet.contains(target)){
                    sensorSet.remove(target);
                }
                else{
                    //do nothing
                }
            }
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SRCDEBUG","serviceonbind");
        return new StatusServiceImpl();
    }

}
