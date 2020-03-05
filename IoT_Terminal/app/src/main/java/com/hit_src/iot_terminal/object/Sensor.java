package com.hit_src.iot_terminal.object;

public class Sensor {
    public static String[] typeList={"床垫","血压","体温","血氧"};

    public int ID;
    public int type;

    public Sensor(int p1, int p2){
        ID=p1;
        type=p2;
    }

    public String toString(){
        return ID +"号" +typeList[type] +"传感器";
    }
}
