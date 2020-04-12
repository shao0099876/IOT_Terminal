package com.hit_src.iot_terminal.object.sensortype;

import java.util.ArrayList;

public class Send {
    public int length;
    public ArrayList<Integer> value;

    public void setLength(String trim) {
        length=Integer.parseInt(trim);
        value=new ArrayList<>();
    }

    public void addValue(String trim) {
        value.add(Integer.parseInt(trim,16));
    }
}
