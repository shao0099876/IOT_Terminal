package com.hit_src.iot_terminal.object.sensortype;

import com.hit_src.iot_terminal.object.sensortype.op.OP;

import java.util.ArrayList;

public class Operation {
    public ArrayList<OP> OPList = new ArrayList<>();

    public void addOP(OP op) {
        OPList.add(op);
    }
}
