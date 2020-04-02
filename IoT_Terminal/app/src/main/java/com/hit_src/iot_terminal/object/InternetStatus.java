package com.hit_src.iot_terminal.object;

public class InternetStatus {
    public boolean connectionStatus;
    public long lastTime;
    public InternetStatus(){
        connectionStatus=false;
        lastTime=0;
    }
}
