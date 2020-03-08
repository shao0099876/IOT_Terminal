package com.hit_src.iot_terminal.profile.status;

import com.hit_src.iot_terminal.tools.MessageThread;
import com.hit_src.iot_terminal.ui.OverviewActivity;
import com.hit_src.iot_terminal.ui.handler.OverviewUIHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class Status {
    private static volatile HashSet<Integer> sensorSet=new HashSet<>();//已连接的传感器ID集合
    private static volatile boolean internetConnection=false;
    private static volatile long internetConnectionLastTime=0;//存毫秒数时间以节省空间

    public static final int SENSOR_LIST=0;
    public static final int INTERNET_CONNECTION=1;
    public static final int INTERNET_CONNECTION_STATUS=2;
    public static final int INTERNET_CONNECTION_LASTTIME=3;

    public static Object getStatusData(int cmd){
        Object res=null;
        switch (cmd){
            case SENSOR_LIST:
                res=sensorSet;
                break;
            case INTERNET_CONNECTION_STATUS:
                res=internetConnection;
                break;
            case INTERNET_CONNECTION_LASTTIME:
                res=time(internetConnectionLastTime);
                break;
        }
        return res;
    }
    public static void setStatusData(int cmd,Object arg){
        switch (cmd){
            case SENSOR_LIST:
                sensorSet= (HashSet<Integer>) arg;
                OverviewActivity.updateSensorStatusShow();
                break;
            case INTERNET_CONNECTION_STATUS:
                internetConnection= (boolean) arg;
                OverviewActivity.updateInternetStatusShow();
                break;
            case INTERNET_CONNECTION_LASTTIME:
                internetConnectionLastTime= (long) arg;
                OverviewActivity.updateInternetStatusShow();
                break;
        }
    }
    public static void setStatusData(int cmd,Object arg1,Object arg2){
        switch (cmd){
            case INTERNET_CONNECTION:
                internetConnection= (boolean) arg1;
                internetConnectionLastTime= (long) arg2;
                OverviewActivity.updateInternetStatusShow();
                break;
        }
    }
    private static String time(long time){
        Date date=new Date(time);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
