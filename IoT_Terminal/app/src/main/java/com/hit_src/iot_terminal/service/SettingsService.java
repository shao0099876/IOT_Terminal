package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SettingsService extends Service {
    private SharedPreferences settingsFile;

    private class CMDStruct{
        int cmd;
        ArrayList<Object> args;

        static final int setUpperServerAddr=0;
        static final int setUpperServerPort=1;
        static final int setSerialQuerySetting=2;

        CMDStruct(int cmd){
            this.cmd=cmd;
            args=new ArrayList<>();
        }
        void put(Object arg){
            args.add(arg);
        }
    }
    private LinkedBlockingQueue<CMDStruct> blockingQueue=new LinkedBlockingQueue<>();
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                CMDStruct cmd= null;
                try {
                    cmd = blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assert cmd != null;
                switch(cmd.cmd){
                    case CMDStruct.setUpperServerAddr:{
                        String addr= (String) cmd.args.get(0);
                        SharedPreferences.Editor editor=settingsFile.edit();
                        editor.putString("UpperServerAddr",addr);
                        editor.apply();
                        break;
                    }
                    case CMDStruct.setUpperServerPort:{
                        int port= (int) cmd.args.get(0);
                        SharedPreferences.Editor editor=settingsFile.edit();
                        editor.putInt("UpperServerPort",port);
                        editor.apply();
                        break;
                    }
                    case CMDStruct.setSerialQuerySetting:{
                        boolean setting= (boolean) cmd.args.get(0);
                        SharedPreferences.Editor editor=settingsFile.edit();
                        editor.putBoolean("SerialQuerySetting",setting);
                        editor.apply();
                        break;
                    }
                }
            }
        }
    });
    @Override
    public void onCreate(){
        super.onCreate();
        settingsFile=getSharedPreferences("settings",MODE_PRIVATE);
        mainThread.start();
    }
    public class SettingsServiceImpl extends ISettingsService.Stub {

        @Override
        public String getUpperServerAddr() {
            return settingsFile.getString("UpperServerAddr","");
        }

        @Override
        public void setUpperServerAddr(String addr) {
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setUpperServerAddr);
            cmdStruct.put(addr);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getUpperServerPort() {
            return settingsFile.getInt("UpperServerPort",-1);
        }

        @Override
        public void setUpperServerPort(int port) {
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setUpperServerPort);
            cmdStruct.put(port);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean getSerialQuerySetting() {
            return settingsFile.getBoolean("SerialQuerySetting",true);
        }

        @Override
        public void setSerialQuerySetting(boolean setting) {
            CMDStruct cmdStruct=new CMDStruct(CMDStruct.setSerialQuerySetting);
            cmdStruct.put(setting);
            try {
                blockingQueue.put(cmdStruct);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SettingsServiceImpl();
    }
}
