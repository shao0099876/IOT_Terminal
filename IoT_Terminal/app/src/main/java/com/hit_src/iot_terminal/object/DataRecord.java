package com.hit_src.iot_terminal.object;

public class DataRecord {
    public long time;
    public Integer data;
    public int sensorID;

    public DataRecord(int sensorID, long time, Integer data) {
        this.time = time;
        this.data = data;
        this.sensorID = sensorID;
    }
}
