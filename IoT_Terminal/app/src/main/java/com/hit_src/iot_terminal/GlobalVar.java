package com.hit_src.iot_terminal;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.Datatype;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.service.DatabaseService;
import com.hit_src.iot_terminal.tools.Filesystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

public class GlobalVar {
    public volatile static ObservableArrayList<Sensor> sensorList = new ObservableArrayList<>();
    public volatile static ObservableArrayList<String> logList = new ObservableArrayList<>();
    public volatile static ObservableBoolean internetConnectionStatus = new ObservableBoolean();
    public volatile static HashMap<Integer, SensorType> sensorTypeHashMap = new HashMap<>();
    public volatile static HashMap<String, Integer> xmlRecordHashMap = new HashMap<>();
    static{
        sensorList.addAll(DatabaseService.getInstance().getSensorList());
    }

    public static int getSensorConnectedAmount() {
        int res = 0;
        for (Sensor i : sensorList) {
            if (i.isConnected()) {
                res += 1;
            }
        }
        return res;
    }

    public static String getLog() {
        StringBuilder sb = new StringBuilder();
        for (String i : logList) {
            sb.append(i + "\n");
        }
        return sb.toString();
    }

    public static void log(String s) {
        if (logList.size() > 100) {
            logList.remove(0);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss->", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        logList.add(simpleDateFormat.format(new Date()) + s);
    }

    public static void removeSensor(int id) {
        for (Sensor i : sensorList) {
            if (i.getID() == id) {
                sensorList.remove(i);
                break;
            }
        }
    }

    public static void updateSensor(Sensor sensor) {
        for (int i = 0; i < sensorList.size(); i++) {
            if (sensorList.get(i).getID() == sensor.getID()) {
                sensorList.set(i, sensor);
            }
        }
    }

    public static ArrayList<SensorType> getSensorTypeList() {
        ArrayList<SensorType> res=new ArrayList<>();
        res.addAll(sensorTypeHashMap.values());
        return res;
    }

    public static void addSensor(SensorType sensorType, int parseInt) {
        int id=0;
        for(Sensor i:sensorList){
            id=id<=i.getID()?i.getID()+1:id;
        }
        Sensor now=new Sensor();
        now.setID(id);
        now.setType(sensorType.id);
        now.setLoraAddr(parseInt);
        now.setEnabled(true);
        sensorList.add(now);
    }

    public static ArrayList<Datatype> getDataTypeList() {
        HashSet<Datatype> res=new HashSet<>();
        for(SensorType i:sensorTypeHashMap.values()){
            res.add(i.data);
        }
        return new ArrayList<>(Arrays.asList(res.toArray(new Datatype[0])));
    }

    public static ArrayList<Sensor> getSensorListbyDatatype(String name) {
        HashSet<SensorType> sensorTypes=new HashSet<>();
        for(SensorType i:sensorTypeHashMap.values()){
            if(i.data.name.equals(name)){
                sensorTypes.add(i);
            }
        }
        ArrayList<Sensor> res=new ArrayList<>();
        for(SensorType i:sensorTypes){
            for(Sensor j:sensorList){
                if(j.getType()==i.id){
                    res.add(j);
                }
            }
        }
        return res;
    }
}
