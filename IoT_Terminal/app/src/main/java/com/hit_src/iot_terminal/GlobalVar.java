package com.hit_src.iot_terminal;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.tools.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class GlobalVar {
    public static ObservableArrayList<XMLRecord> xmlRecords=new ObservableArrayList<>();
    public static HashMap<String,Integer> xmlRecordtoSensorTypeMap=new HashMap<>();
    public static ObservableArrayMap<Integer,SensorType> sensorTypeMap=new ObservableArrayMap<>();
    public static ObservableArrayMap<Integer, Sensor> sensorMap=new ObservableArrayMap<>();
    public static ObservableBoolean internetStatus=new ObservableBoolean();
    public static ObservableArrayList<String> logArrayList=new ObservableArrayList<>();
    static{
        new GlobalVar();
    }
    public GlobalVar(){
        internetStatus.set(false);
        xmlRecords.addAll(PackageManager.readLocalXMLList());
    }
    public static void addLogLiveData(String p){
        if(logArrayList.size()>10){
            logArrayList.remove(0);
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss >", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        logArrayList.add(simpleDateFormat.format(new Date())+p+"\n");
    }
}
