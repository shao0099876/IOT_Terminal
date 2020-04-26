package com.hit_src.iot_terminal.object.sensortype.op;

public class Div extends OP {
    public Div(String trim) {
        super(trim);
    }

    @Override
    public int calculate(int reg, byte[] source) {
        if (isMemory) {
            return reg / source[value];
        } else {
            return reg / value;
        }
    }
}
