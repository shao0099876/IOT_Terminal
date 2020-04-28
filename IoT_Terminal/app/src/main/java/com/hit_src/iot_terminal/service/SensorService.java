package com.hit_src.iot_terminal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.hardware.SerialPort;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.overview.OverviewViewModel;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class SensorService extends Service {
    public static ArrayList<Sensor> sensors=new ArrayList<>();
    public static DataRecord getRealtimeData(int id) {
        if (realtimeData.sensorID == id) {
            return realtimeData;
        }
        return null;
    }

    public static DataRecord getRealtimeData(ArrayList<Sensor> sensors) {
        boolean check = false;
        for (Sensor i : sensors) {
            if (i.getID() == realtimeData.sensorID) {
                check = true;
                break;
            }
        }
        if (check) {
            return realtimeData;
        }
        return null;
    }

    private static DataRecord realtimeData;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensors.addAll(GlobalVar.sensorList.subList(0,GlobalVar.sensorList.size()));
        mainThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mainThread.interrupt();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Thread mainThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (MainApplication.self!=null) {
                for(Sensor i:sensors){
                    if(!i.isEnabled()){
                        continue;
                    }
                    send(i);
                    if(recv(i)){
                        i.setConnected(true);
                        GlobalVar.log(i.getID() + "号传感器交互成功");
                    } else {
                        i.setConnected(false);
                        GlobalVar.log(i.getID() + "号传感器交互失败");
                    }
                    GlobalVar.updateSensor(i);

                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sensors.clear();
                sensors.addAll(GlobalVar.sensorList.subList(0,GlobalVar.sensorList.size()));
            }
        }
    });

    private void send(Sensor i) {
        byte[] cmd = i.packageCMD();
        SerialPort.write(cmd);
    }

    private boolean recv(Sensor i) {
        byte[] raw_data = SerialPort.read(GlobalVar.sensorTypeHashMap.get(i.getType()).recv.length);
        if (raw_data == null) {
            return false;
        }
        int res = i.unpackage(raw_data);
        //invalid data
        if (res >= 4500) {
            realtimeData = new DataRecord(i.getID(), new Date().getTime(), null);
            return true;
        }
        realtimeData = new DataRecord(i.getID(), new Date().getTime(), res);
        DatabaseService.getInstance().addDataRecord(realtimeData);
        return true;
    }

}
