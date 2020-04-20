package com.hit_src.iot_terminal.object;

public class DataRecord {
    public final long time;
    public final Integer data;
    public final int sensorID;

    public DataRecord(int sensorID, long time, Integer data) {
        this.time=time;
        this.data=data;
        this.sensorID=sensorID;
    }
    
}
