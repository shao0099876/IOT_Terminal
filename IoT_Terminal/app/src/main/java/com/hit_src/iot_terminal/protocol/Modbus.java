package com.hit_src.iot_terminal.protocol;

import android.util.Log;

public class Modbus {
    public static byte[] exec(byte[] upper) {
        byte addr=upper[0];
        byte func=upper[1];
        byte[] origin_crc=new byte[2];
        origin_crc[0]=upper[upper.length-2];
        origin_crc[1]=upper[upper.length-1];
        byte[] crc=CRC(upper,upper.length-2);
        if(crc[0]!=origin_crc[0]||crc[1]!=origin_crc[1]){
            Log.e("Modbus","CRC Error!");
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
        byte[] out_crc=CRC(reply,reply.length-2);
        reply[reply.length-2]=out_crc[0];
        reply[reply.length-1]=out_crc[1];
        return reply;
    }
    private static byte[] CRC(byte[] data,int len){
        short crc= (short) 0xffff;  //1)	预置16位CRC校验寄存器为FFFF.
        for(int i=0;i<len;i++){     //5)	重复步骤2-4，对下一个8位数据进行处理。
            //2)	把第1个8位数据与CRC校验寄存器的低8位数据异或，结果放于CRC校验寄存器。
            crc= (short) ((crc&0xff00)+(i^(crc&0x00ff)&0x00ff));
            for(int j=1;j<=8;j++){  //4)	重复步骤3，直到右移8次。
                //3)	CRC校验寄存器内容右移1位，检查寄存器最低位，如果为1，则与0xA001异或；如果为0，无需异或。
                crc>>=1;
                if((crc&0x0001)==1){
                    crc^=0xA001;
                }
            }
        }
        //6)	最后得到的CRC寄存器即为CRC码
        byte[] res=new byte[2];
        res[0]= (byte) (crc&0x00ff);
        res[1]= (byte) ((crc&0xff00)>>8);
        return res;
    }
}
