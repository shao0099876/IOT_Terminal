package com.hit_src.iot_terminal.object;

public class Sensor {
    public static String dbCreateSQL="CREATE TABLE Sensor ( sensor_id INTEGER PRIMARY KEY, sensor_type integer NOT NULL );";
    private static String[] typeList={"床垫","血压","体温","血氧"};

    private int ID;
    private int type;

    public Sensor(){
        ID=-1;
        type=-1;
    }

    public Sensor(int p1, int p2){
        ID=p1;
        type=p2;
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(ID);
        sb.append("号");
        sb.append(typeList[type]);
        sb.append("传感器");
        return sb.toString();
    }
    public String id_toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(ID);
        return sb.toString();
    }

}
