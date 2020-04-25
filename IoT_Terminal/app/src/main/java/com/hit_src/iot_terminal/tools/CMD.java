package com.hit_src.iot_terminal.tools;

public class CMD {
    public byte addr;
    public byte func;
    public byte[] data;
    public byte[] crc;

    public byte[] toCRCRaw() {
        byte[] res=new byte[2+data.length];
        res[0]=addr;
        res[1]=func;
        for(int i=0;i<data.length;i++){
            res[i+2]=data[i];
        }
        return res;
    }

    public byte[] toByteArray() {
        byte[] res=new byte[4+data.length];
        res[0]=addr;
        res[1]=func;
        for(int i=0;i<data.length;i++){
            res[i+2]=data[i];
        }
        for(int i=0;i<2;i++){
            res[i+2+data.length]=crc[i];
        }
        return res;
    }
}
