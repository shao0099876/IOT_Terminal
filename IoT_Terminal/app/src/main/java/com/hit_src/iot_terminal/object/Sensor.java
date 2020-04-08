package com.hit_src.iot_terminal.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;

public class Sensor{

    //database attribute
    private int ID;
    private String type;
    private int loraAddr;
    //setting&db attribute
    private int enabled;
    //status attribute
    private boolean connected;

    public Sensor(){
        connected=false;
    }


    public int getID(){
        return ID;
    }
    public String getType(){
        return type;
    }
    public int getLoraAddr(){
        return loraAddr;
    }
    public boolean isEnabled(){
        return enabled==0?false:true;
    }
    public boolean isConnected(){
        return connected;
    }

    public void setID(int p){
        ID=p;
    }
    public void setType(String p){
        type=p;
    }
    public void setLoraAddr(int p){
        loraAddr=p;
    }
    public void setEnabled(boolean p){
        if(p){
            enabled=1;
        }
        else{
            enabled=0;
        }
    }
    public void setConnected(boolean p){
        connected=p;
    }


    public int sendLength=1;
    public int replyLength=2;


    public byte[] packageCMD() {
        ByteBuffer buffer=ByteBuffer.allocate(3+sendLength);
        buffer.put((byte)((loraAddr>>=4)&15));
        buffer.put((byte)(loraAddr&15));
        buffer.put((byte)0x01);
        buffer.put((byte)0x55);
        return buffer.array();
    }

    public int unpackage(byte[] raw_data) {
        int res=0;
        int high=raw_data[0]<<8;
        high&=0x0FFFF;
        int low=raw_data[1];
        low&=0x0FFFF;
        res=high|low;
        return res;
    }
}
