package com.hit_src.iot_terminal.protocol;

import com.hit_src.iot_terminal.GlobalVar;

import java.util.ArrayList;

public class Modbus {
    public static byte[] exec(byte[] upper) {
        StringBuilder sb=new StringBuilder();
        sb.append("接收到Modbus指令：");
        for(byte i:upper){
            sb.append(i);
            sb.append(" ");
        }
        GlobalVar.addLogLiveData(sb.toString());

        byte addr=upper[0];
        byte func=upper[1];
        byte[] tmp=new byte[upper.length-2];
        for(int i=0;i<tmp.length;i++){
            tmp[i]=upper[i];
        }
        byte[] crc= CRC(tmp);
        boolean crc_check=true;
        for(int i=0;i<2;i++){
            if(upper[i+upper.length-2]!=crc[i]){
                crc_check=false;
            }
        }
        if(!crc_check){
            GlobalVar.addLogLiveData("CRCError!");
            return upper;
        }
        byte[] data=null;
        switch(func){
            case 0x03:
                int start=0+upper[2];
                start+=((int)upper[3])*256;
                int len=0+upper[4];
                len+=((int)upper[5])*256;
                if(start==0xFFFF){
                    data=new byte[4];
                    int raw=GlobalVar.sensors.size();
                    for(int i=0;i<4;i++){
                        data[i]= (byte) (raw&0x0ff);
                        raw>>=8;
                    }
                }
                break;
        }
        if(data==null){
            return null;
        }
        byte[] reply=new byte[2+data.length+2];
        reply[0]=addr;
        reply[1]=func;
        for(int i=0;i<data.length;i++){
            reply[2+i]=data[i];
        }
        byte[] out_crc=CRC(reply);
        reply[reply.length-2]=out_crc[0];
        reply[reply.length-1]=out_crc[1];
        return reply;
    }
    private static byte[] CRC(byte[] raw_data){
        //0) 数据转按位存储
        ArrayList<boolean[]> data=new ArrayList<>();
        for(byte i:raw_data){
            boolean[] tmp=new boolean[8];
            for(int j=0;j<8;j++){
                tmp[j]= (i & 1) == 1;
                i>>=1;
            }
            data.add(tmp);
        }
        boolean[] cnt=new boolean[16];
        int t=0x0A001;
        for(int j=0;j<16;j++){
            cnt[j]=(t&1)==1;
            t>>=1;
        }
        //1)	预置16位CRC校验寄存器为FFFF.
        boolean[] crc=new boolean[16];
        for(int i=0;i<16;i++){
            crc[i]=true;
        }

        for(boolean[] i:data){
            //2)	把第1个8位数据与CRC校验寄存器的低8位数据异或，结果放于CRC校验寄存器。
            for(int j=0;j<8;j++){
                if(crc[j]&&i[j]){
                    crc[j]=false;
                }
                else if(crc[j]&&!i[j]){
                    crc[j]=true;
                }
                else if(!crc[j]&&i[j]){
                    crc[j]=true;
                }
                else if(!crc[j]&&!i[j]){
                    crc[j]=false;
                }
            }
            for(int n=0;n<8;n++){
                //3)	CRC校验寄存器内容右移1位，检查寄存器最低位，如果为1，则与0xA001异或；如果为0，无需异或。
                for(int j=0;j<=14;j++){
                    crc[j]=crc[j+1];
                }
                crc[15]=false;
                if(crc[0]){
                    for(int j=0;j<16;j++){
                        if(crc[j]&&cnt[j]){
                            crc[j]=false;
                        }
                        else if(crc[j]&&!cnt[j]){
                            crc[j]=true;
                        }
                        else if(!crc[j]&&cnt[j]){
                            crc[j]=true;
                        }
                        else if(!crc[j]&&!cnt[j]){
                            crc[j]=false;
                        }
                    }
                }
                //4)	重复步骤3，直到右移8次。
            }
            //5)	重复步骤2-4，对下一个8位数据进行处理。

        }
        //6)	最后得到的CRC寄存器即为CRC码
        byte[] res=new byte[2];
        byte high=0;
        for(int i=15;i>=8;i--){
            if(crc[i]){
                high+=1;
            }
            high*=2;
        }
        res[1]=high;
        byte low=0;
        for(int i=7;i>=0;i--){
            if(crc[i]){
                low+=1;
            }
            low*=2;
        }
        res[0]=low;
        return res;
    }
}
