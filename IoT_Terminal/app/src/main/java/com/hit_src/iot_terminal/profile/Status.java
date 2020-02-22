package com.hit_src.iot_terminal.profile;

import android.os.Handler;

import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.handler.OverviewStatusHandler;

import java.util.HashSet;

public class Status {
    private static volatile HashSet<Integer> Connected_Sensor_Set=new HashSet<>();
    private static volatile Handler Overview_status_change_handler=new Handler();
    public void writeSensorSet(HashSet<Integer> p){
        Connected_Sensor_Set=p;
        MessageThread.sendMessage(Overview_status_change_handler, OverviewStatusHandler.SENSOR_UPDATE);
    }
    public static HashSet<Integer> readSensorSet(){
        return Connected_Sensor_Set;
    }
    public static void registHandler(Handler handler){
        Overview_status_change_handler=handler;
    }
}
