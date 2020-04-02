package com.hit_src.iot_terminal;

public class Global {
    static {

    }
    private static String[] sensorTypeList={"超声传感器"};
    private static String[] dataTypeList={"距离"};

    public static String[] getSensorTypeList(){
        return sensorTypeList;
    }
    public static String[] getDataTypeList(){
        return dataTypeList;
    }

    public static native byte[] CRC(byte[] data);
}
