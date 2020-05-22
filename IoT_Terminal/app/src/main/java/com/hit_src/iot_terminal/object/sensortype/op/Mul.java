package com.hit_src.iot_terminal.object.sensortype.op;

public class Mul extends OP {
    public Mul(String trim) {
        super(trim);
    }

    @Override
    public int calculate(int reg, byte[] source) {
        if (isMemory) {
            return reg * source[value];
        } else {
            return reg * value;
        }
    }
}
