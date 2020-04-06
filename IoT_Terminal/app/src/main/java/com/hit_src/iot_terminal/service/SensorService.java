package com.hit_src.iot_terminal.service;

import android.os.RemoteException;
import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.overview.OverviewViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SensorService extends AbstractRunningService {

    public volatile static ObservableArrayList<Sensor> sensorList=new ObservableArrayList<>();

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
    protected void runOnReady() {
        try {
            sensorList.addAll(MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mainThread.start();
    }
    private Thread mainThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                boolean serialQueryEnabled=false;
                try {
                    serialQueryEnabled=settingsService.getSerialQuerySetting();
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
                    for(int i=0;i<sensorList.size();i++){
                        Sensor now=sensorList.get(i);
                        if(!now.isEnabled()){
                            continue;
                        }
                        send(now);
                        if(recv(now)){
                            now.setConnected(true);
                            OverviewViewModel.addLogLiveData(now.getID()+"号传感器交互成功");
                        }
                        else{
                            now.setConnected(false);
                            OverviewViewModel.addLogLiveData(now.getID()+"号传感器交互失败");
                        }
                        sensorList.set(i,now);
                    }
                }catch (IndexOutOfBoundsException e){

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
        byte[] raw_data=SerialPort.read(i.replyLength);
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
            dbService.addSensorData(i.getID(),res);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        realtimeData=new DataRecord(i.getID(),new Date().getTime(),res);
        return true;
    }

}
