package com.hit_src.iot_terminal.object;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.sensortype.Operation;
import com.hit_src.iot_terminal.object.sensortype.Receive;
import com.hit_src.iot_terminal.object.sensortype.Send;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.object.sensortype.Validity;
import com.hit_src.iot_terminal.object.sensortype.op.OP;

import java.nio.ByteBuffer;

public class Sensor {

    //database attribute
    private int ID;
    private int type;
    private int loraAddr;
    //setting&db attribute
    private int enabled;
    //status attribute
    private boolean connected;

    public Sensor() {
        connected = false;
    }


    public int getID() {
        return ID;
    }

    public void setID(int p) {
        ID = p;
    }

    public int getType() {
        return type;
    }

    public void setType(int p) {
        type = p;
    }

    public int getLoraAddr() {
        return loraAddr;
    }

    public void setLoraAddr(int p) {
        loraAddr = p;
    }

    public boolean isEnabled() {
        return enabled != 0;
    }

    public void setEnabled(boolean p) {
        if (p) {
            enabled = 1;
        } else {
            enabled = 0;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean p) {
        connected = p;
    }

    public byte[] packageCMD() {
        SensorType sensorType = GlobalVar.sensorTypeHashMap.get(type);
        Send send = sensorType.send;
        ByteBuffer buffer = ByteBuffer.allocate(3 + send.length);
        buffer.put((byte) ((loraAddr >>= 4) & 0x0ff));
        buffer.put((byte) (loraAddr & 0x0ff));
        buffer.put((byte) 0x01);
        for (int i = 0; i < send.length; i++) {
            buffer.put(send.value.get(i));
        }
        return buffer.array();
    }

    public int unpackage(byte[] raw_data) {
        SensorType sensorType = GlobalVar.sensorTypeHashMap.get(type);
        Receive recv = sensorType.recv;
        int reg = 0;
        Operation operation = recv.operation;
        for (OP i : operation.OPList) {
            reg = i.calculate(reg, raw_data);
            reg &= 0x0FFFF;
        }
        return reg;
    }

    public boolean isInvalid(int res) {
        SensorType sensorType = GlobalVar.sensorTypeHashMap.get(type);
        Validity validity=sensorType.validity;
        return !validity.judge(res);
    }
}
