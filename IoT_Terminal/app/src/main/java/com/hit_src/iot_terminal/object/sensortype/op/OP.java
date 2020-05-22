package com.hit_src.iot_terminal.object.sensortype.op;

public abstract class OP {
    public boolean isMemory;
    public int value;

    public OP(String trim) {
        String tmp;
        if (trim.charAt(0) == '[') {
            isMemory = true;
            tmp = trim.substring(1, trim.length() - 1);
        } else {
            tmp = trim;
        }
        value = Integer.parseInt(tmp);
    }

    public abstract int calculate(int reg, byte[] source);
}
