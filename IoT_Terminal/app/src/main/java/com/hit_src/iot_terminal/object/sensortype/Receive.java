package com.hit_src.iot_terminal.object.sensortype;

public class Receive {
    public int length;
    public Operation operation;

    public void setLength(String trim) {
        length = Integer.parseInt(trim);
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
