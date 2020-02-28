package com.hit_src.iot_terminal.profile.status;

import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.OverviewActivity;
import com.hit_src.iot_terminal.ui.handler.OverviewStatusHandler;

import java.util.HashSet;

public class Status {
    private static volatile HashSet<Integer> Connected_Sensor_Set=new HashSet<>();
    private static volatile IPStatus ipStatus=new IPStatus();

    public void writeSensorSet(HashSet<Integer> p){
        Connected_Sensor_Set=p;
        MessageThread.sendMessage(OverviewActivity.handler, OverviewStatusHandler.SENSOR_UPDATE);
    }
    public static HashSet<Integer> readSensorSet(){
        return Connected_Sensor_Set;
    }
    public void writeInternetStatus(IPStatus status){
        ipStatus=status;
        MessageThread.sendMessage(OverviewActivity.handler,OverviewStatusHandler.INTERNET_UPDATE);
    }
    public static IPStatus readInternetStatus(){
        return ipStatus;
    }
}
