package com.hit_src.iot_terminal.object.sensortype;

import java.util.ArrayList;

public class Send {
    public int length;
    public ArrayList<Byte> value;

    public void setLength(String trim) {
        length = Integer.parseInt(trim);
        value = new ArrayList<>();
    }

    public void addValue(String trim) {
        value.add(Byte.parseByte(trim, 16));
    }
}
