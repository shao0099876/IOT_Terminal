package com.hit_src.iot_terminal.profile.status;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IPStatus {
    private static volatile boolean InternetConnection=false;
    private static volatile long InternetConnectionLastTime=0;//存毫秒数时间以节省空间
    public static void writeIPStatus(boolean internetConnection,long internetConnectionLastTime){
        InternetConnection=internetConnection;
        InternetConnectionLastTime=internetConnectionLastTime;
    }

    public boolean connected() {
        return InternetConnection;
    }
    public String time(){
        Date date=new Date(InternetConnectionLastTime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
