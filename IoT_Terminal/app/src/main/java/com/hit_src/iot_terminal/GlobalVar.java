package com.hit_src.iot_terminal;

import android.os.RemoteException;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableMap;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.tools.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class GlobalVar {
    public static ObservableArrayList<XMLRecord> xmlRecords=new ObservableArrayList<>();
    public static ObservableArrayList<SensorType> sensorTypes=new ObservableArrayList<>();
    public static ObservableArrayList<Sensor> sensors=new ObservableArrayList<>();
    public static ObservableBoolean internetStatus=new ObservableBoolean();
    public static ObservableArrayList<String> logArrayList=new ObservableArrayList<>();
    private static GlobalVar self=null;
    private GlobalVar(){
        //xmlRecords
        xmlRecords.addAll(PackageManager.getInstance().buildXMLRecords());
        //sensorTypes
        sensorTypes.addAll(PackageManager.getInstance().buildSensorTypes());
        //sensors
        try {
            sensors.addAll(MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //internetStatus
        internetStatus.set(false);
    }
    public static void addLogLiveData(String p){
        if(logArrayList.size()>10){
            logArrayList.remove(0);
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss >", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        logArrayList.add(simpleDateFormat.format(new Date())+p+"\n");
    }

    public static void createInstance() {
        if(self==null){
            self=new GlobalVar();
        }
    }
}
