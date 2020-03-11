package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

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

    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SRCDEBUG","serviceonbind");
        return new StatusServiceImpl();
    }

//    public static final int GetSensorList=0;
//    public static final int GetInternetConnectionStatus=1;
//    public static final int GetInternetConnectionLasttime=2;
/*
    private Messenger messenger=new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=new Bundle();
            Messenger reply=msg.replyTo;
            Message replymsg=Message.obtain();
            boolean callback=false;
            switch (msg.what){
                case GetSensorList:{
                    int size=sensorSet.size();
                    int[] id=new int[size];
                    for(int i=0;i<size;i++){
                        id[i]=sensorSet.get(i);
                    }
                    bundle.putIntArray("id",id);
                    callback=true;
                    break;
                }
                case GetInternetConnectionStatus:{
                    bundle.putBoolean("connectionstatus",internetConnection);
                    callback=true;
                    break;
                }
                case GetInternetConnectionLasttime:{
                    bundle.putLong("connectionlasttime",internetConnectionLasttime);
                    callback=true;
                    break;
                }
                default:{

                }
            }
            if(callback){
                try {
                    reply.send(replymsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    });*/

}
