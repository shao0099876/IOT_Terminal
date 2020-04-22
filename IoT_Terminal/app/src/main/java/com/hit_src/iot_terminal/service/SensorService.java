package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SensorService extends Service {

    public static DataRecord getRealtimeData(int id) {
        if(realtimeData.sensorID==id){
            return realtimeData;
        }
        return null;
    }

    public static DataRecord getRealtimeData(ArrayList<Sensor> sensors) {
        boolean check=false;
        for(Sensor i:sensors){
            if(i.getID()==realtimeData.sensorID){
                check=true;
                break;
            }
        }
        if(check){
            return realtimeData;
        }
        return null;
    }
    private static DataRecord realtimeData;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mainThread.start();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        mainThread.interrupt();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private final Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                boolean serialQueryEnabled=false;
                try {
                    serialQueryEnabled=MainApplication.settingsService.getSerialQuerySetting();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(!serialQueryEnabled){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                try{
                    for(int i=0;i<GlobalVar.sensors.size();i++){
                        Sensor now=GlobalVar.sensors.get(i);
                        if(!now.isEnabled()){
                            continue;
                        }
                        send(now);
                        if(recv(now)){
                            now.setConnected(true);
                            GlobalVar.addLogLiveData(now.getID()+"号传感器交互成功");
                        }
                        else{
                            now.setConnected(false);
                            GlobalVar.addLogLiveData(now.getID()+"号传感器交互失败");
                        }

                        GlobalVar.sensors.set(i,now);
                    }
                } catch (NullPointerException e){

                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private void send(Sensor i){
        byte[] cmd=i.packageCMD();
        SerialPort.write(cmd);
    }
    private boolean recv(Sensor i){
        SensorType sensorType=null;
        for(SensorType j:GlobalVar.sensorTypes){
            if(j.id==i.getType()){
                sensorType=j;
                break;
            }
        }
        assert sensorType != null;
        byte[] raw_data=SerialPort.read(sensorType.recv.length);
        if(raw_data==null){
            return false;
        }
        int res=i.unpackage(raw_data);
        //invalid data
        if(res>=4500){
            realtimeData=new DataRecord(i.getID(),new Date().getTime(),null);
            return true;
        }
        try {
            MainApplication.dbService.addSensorData(i.getID(),res);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        realtimeData=new DataRecord(i.getID(),new Date().getTime(),res);
        return true;
    }

}
