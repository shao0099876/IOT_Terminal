package com.hit_src.iot_terminal.object.sensortype.op;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.object.sensortype.Operation;

public class Div extends OP {
    public Div(String trim) {
        super(trim);
    }

    @Override
    public int calculate(int reg, int[] source) {
        if(isMemory){
            return reg/source[value];
        }
        else {
            return reg/value;
        }
    }
}
