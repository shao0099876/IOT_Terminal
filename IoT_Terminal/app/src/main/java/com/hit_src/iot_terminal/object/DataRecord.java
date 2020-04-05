package com.hit_src.iot_terminal.object;

public class DataRecord {
    public long time;
    public int data;
    public int sensorID;

    public DataRecord(int sensorID, long time, int data) {
        this.time=time;
        this.data=data;
        this.sensorID=sensorID;
    }

    public String formatSensorName() {
        return "传感器#"+sensorID;
    }
}
