package com.hit_src.iot_terminal.protocol;

import android.util.Log;

import com.hit_src.iot_terminal.Global;

public class Modbus {
    public static byte[] exec(byte[] upper) {
        byte addr=upper[0];
        byte func=upper[1];
        byte[] tmp=new byte[upper.length-2];
        for(int i=0;i<tmp.length;i++){
            tmp[i]=upper[i];
        }
        byte[] crc= Global.CRC(tmp);
        boolean crc_check=true;
        for(int i=0;i<2;i++){
            if(upper[i+upper.length-2]!=crc[i]){
                crc_check=false;
            }
        }
        if(!crc_check){
            Log.e("SRCDEBUG","CRCError");
            return null;
        }
        byte[] data=null;
        switch(func){
            case 0x03:
                short off=(short)(upper[3]<<8 | upper[2]);
                short len=(short)(upper[5]<<8 | upper[4]);
                //read();
                data=new byte[1];
                data[0]=0x10;
                break;
        }
        byte[] reply=new byte[2+data.length+2];
        reply[0]=addr;
        reply[1]=func;
        for(int i=0;i<data.length;i++){
            reply[2+i]=data[i];
        }
        byte[] out_crc=Global.CRC(reply);
        reply[reply.length-2]=out_crc[0];
        reply[reply.length-1]=out_crc[1];
        return reply;
    }
}
