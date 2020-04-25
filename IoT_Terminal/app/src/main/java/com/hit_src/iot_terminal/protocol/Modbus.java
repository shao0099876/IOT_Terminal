package com.hit_src.iot_terminal.protocol;

import android.util.Log;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.SensorService;
import com.hit_src.iot_terminal.tools.CMD;

import static com.hit_src.iot_terminal.tools.BitOperation.*;

public class Modbus {
    public static byte[] exec(byte[] upper) {
        byte addr=upper[0];
        byte func=upper[1];
        byte[] crc= CRC(subArray(upper,0,upper.length-2));
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
                int off= BytestoInteger(subArray(upper,2,2));
                int len= BytestoInteger(subArray(upper,4,2));
                data=work_03(off,len);
                break;
        }
        if(data==null){
            return null;
        }
        CMD reply=new CMD();
        reply.addr=addr;
        reply.func=func;
        reply.data=data;
        reply.crc=CRC(reply.toCRCRaw());
        return reply.toByteArray();
    }
    private static byte[] work_03(int off,int len){
        if(off==0xffff&&len==1){
            int raw= SensorService.sensorList.size();
            return IntegertoBytes(raw,4);
        } else{
            byte[] res=new byte[0];
            for(int i=0;i<len;i++){
                Sensor now=SensorService.sensorList.get(i+off);
                res=catArray(res,IntegertoBytes(now.getID(),4));
                res=catArray(res,IntegertoBytes(now.getType(),4));
                res=catArray(res,IntegertoBytes(now.getLoraAddr(),2));
                res=catArray(res,IntegertoBytes(now.isEnabled()?1:0,1));
                res=catArray(res,IntegertoBytes(now.isConnected()?1:0,1));
            }
            return res;
        }
    }
    private static byte[] CRC(byte[] raw_data){
        //0) 数据转按位存储
        boolean[][] data=new boolean[raw_data.length][];
        for(int i=0;i<raw_data.length;i++){
            data[i]= BytetoBits(raw_data[i]);
        }
        boolean[][] cnt= BytestoBits(IntegertoBytes(0x0A001,2));
        //1)	预置16位CRC校验寄存器为FFFF.
        boolean[][] crc= BytestoBits(IntegertoBytes(0x0FFFF,2));

        for(boolean[] i:data){
            //2)	把第1个8位数据与CRC校验寄存器的低8位数据异或，结果放于CRC校验寄存器。
            crc[0]= XOR(crc[0],i);
            for(int n=0;n<8;n++){
                //3)	CRC校验寄存器内容右移1位，检查寄存器最低位，如果为1，则与0xA001异或；如果为0，无需异或。
                crc= RightMoves(crc,1);
                if(crc[0][0]){
                    crc= XORS(crc,cnt);
                }
                //4)	重复步骤3，直到右移8次。
            }
            //5)	重复步骤2-4，对下一个8位数据进行处理。
        }
        //6)	最后得到的CRC寄存器即为CRC码
        return BitstoBytes(crc);
    }
    private static byte[] subArray(byte[] p, int start, int len){
        byte[] res=new byte[len];
        for(int i=0;i<len;i++){
            res[i]=p[start+i];
        }
        return res;
    }
    private static byte[] catArray(byte[] p,byte[] p1){
        byte[] res=new byte[p.length+p1.length];
        for(int i=0;i<p.length;i++){
            res[i]=p[i];
        }
        for(int i=0;i<p1.length;i++){
            res[i+p.length]=p1[i];
        }
        return res;
    }
}
