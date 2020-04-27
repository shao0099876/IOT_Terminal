package com.hit_src.iot_terminal;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class GlobalVar {
    public volatile static ObservableArrayList<Sensor> sensorList = new ObservableArrayList<>();
    public volatile static ObservableArrayList<String> logList = new ObservableArrayList<>();
    public volatile static ObservableBoolean internetConnectionStatus = new ObservableBoolean();
    public static HashMap<Integer, SensorType> sensorTypeHashMap = new HashMap<>();
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
        for(Sensor i:sensorList){
            if(i.getID()==id){
                sensorList.remove(i);
                break;
            }
        }
    }
}
