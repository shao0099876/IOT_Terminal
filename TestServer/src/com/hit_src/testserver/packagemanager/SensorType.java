package com.hit_src.testserver.packagemanager;

public class SensorType {
    public int id;
    public String name;
    public Datatype data;

    public void setID(String trim) {
        id=Integer.parseInt(trim);
    }

    public void setName(String trim) {
        name=trim;
    }

    public void setDataType(Datatype data) {
        this.data=data;
    }
}
