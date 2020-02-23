package com.hit_src.iot_terminal.object;

public class Sensor {
    public static String dbCreateSQL="CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type integer NOT NULL );";
    public static String[] typeList={"床垫","血压","体温","血氧"};

    private int ID;
    private int type;

    public Sensor(int p1, int p2){
        ID=p1;
        type=p2;
    }

    public String toString(){
        return ID +"号" +typeList[type] +"传感器";
    }

    public int getID() {
        return ID;
    }

    public int getTypeNum() {
        return type;
    }
}
