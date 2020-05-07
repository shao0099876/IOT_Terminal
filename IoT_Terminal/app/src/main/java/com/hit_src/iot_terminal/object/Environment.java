package com.hit_src.iot_terminal.object;

public class Environment {
    public Sensor sensor;
    public int value;
    public Environment(Sensor i, int res) {
        sensor=i;
        value=res;
    }
}
