package com.hit_src.iot_terminal.service;

import android.os.RemoteException;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SensorService extends AbstractRunningService {
    public static ObservableInt sensorConnectedAmount=new ObservableInt();
    public static ObservableInt sensorAmount=new ObservableInt();
    public static ObservableArrayList<Sensor> sensorList=new ObservableArrayList<>();
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
                ArrayList<Sensor> sensorList=null;
                try {
                    sensorList= (ArrayList<Sensor>) dbService.getSensorList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                for(Sensor i:sensorList){
                    send(i);
                    recv(i);
                    //statusService.setSensorStatus(i.ID,true);
                }
                try {
                    sleep(3000);
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
    private void recv(Sensor i){
        byte[] raw_data=SerialPort.read(i.replyLength);
        int res=i.unpackage(raw_data);
        try {
            dbService.addSensorData(i.ID,res);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void runOnReady() {
        sensorConnectedAmount.set(0);
        try {
            sensorAmount.set(MainApplication.dbService.getSensorAmount());
            sensorList.addAll(MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mainThread.start();
    }
}
