package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class StatusService extends Service {

    private static volatile ArrayList<Integer> sensorSet=new ArrayList<>();
    private static volatile boolean internetConnection=false;
    private static volatile long internetConnectionLasttime=0;

    private class CMDStruct{
        int cmd;
        ArrayList<Object> args;
        static final int setInternetConnectionStatus=0;
        static final int setInternetConnectionLasttime=1;
        static final int setSensorStatus=2;

        CMDStruct(int cmd){
            this.cmd=cmd;
            args=new ArrayList<>();
        }
        void put(Object arg){
            args.add(arg);
        }
    }

    private static LinkedBlockingQueue<CMDStruct> blockingQueue=new LinkedBlockingQueue<>();
    private static Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                CMDStruct cmd= null;
                try {
                    cmd = blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (cmd.cmd){
                    case CMDStruct.setInternetConnectionStatus:{
                        internetConnection= (boolean) cmd.args.get(0);
                        break;
                    }
                    case CMDStruct.setInternetConnectionLasttime:{
                        internetConnectionLasttime= (long) cmd.args.get(0);
                        break;
                    }
                    case CMDStruct.setSensorStatus:{
                        int target= (int) cmd.args.get(0);
                        boolean status= (boolean) cmd.args.get(1);
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
                        break;
                    }
                }
            }
        }
    });

    @Override
    public void onCreate(){
        super.onCreate();
        mainThread.start();
    }

    public class StatusServiceImpl extends IStatusService.Stub{
        @Override
        public ArrayList<Integer> getSensorList() {
            return sensorSet;
        }

        @Override
        public void setSensorStatus(int target, boolean status){
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setSensorStatus);
            cmdStruct.put(target);
            cmdStruct.put(status);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean getInternetConnectionStatus(){
            return internetConnection;
        }

        @Override
        public void setInternetConnectionStatus(boolean status) {
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setInternetConnectionStatus);
            cmdStruct.put(status);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public long getInternetConnectionLasttime() {
            return internetConnectionLasttime;
        }

        @Override
        public void setInternetConnectionLasttime(long lasttime) {
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setInternetConnectionLasttime);
            cmdStruct.put(lasttime);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    @Override
    public IBinder onBind(Intent intent) {
        return new StatusServiceImpl();
    }

}
