package com.hit_src.testserver;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class ModbusSocket {
    private Socket mainSocket=null;
    private InputStream inputStream;
    private OutputStream outputStream;
    public ModbusSocket(Socket socket){
        mainSocket=socket;
        try {
            inputStream=mainSocket.getInputStream();
            outputStream=mainSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ModbusSocket(){
    }

    public void stop() throws IOException {
        inputStream.close();
        outputStream.close();
        if(mainSocket!=null){
            mainSocket.close();
        }
    }

    public void readDeviceCnt() {
        CMD cmd=new CMD();
        cmd.addr=0x01;
        cmd.func=0x03;
        cmd.data=new byte[]{(byte)0xFF,(byte)0xFF,(byte)0x01,(byte)0x00};
        cmd.crc=CRC(cmd.toCRCRaw());
        try {
            outputStream.write(cmd.toByteArray());
            outputStream.flush();
            byte[] reply=new byte[6];
            inputStream.read(reply);
            StringBuilder stringBuilder=new StringBuilder();
            for(byte i:reply){
                stringBuilder.append(i);
                stringBuilder.append(" ");
            }
            Log.d(stringBuilder.toString());
            Log.d("-----Modbus数据包解析-----");
            Log.d("从机地址："+reply[0]);
            Log.d("功能码："+reply[1]);
            Log.d("功能：读设备数量");
            Terminal.DevCnt=0;
            Terminal.DevCnt+=reply[3];
            Terminal.DevCnt+=reply[4]*256;
            Log.d(Terminal.toLog());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] CRC(byte[] raw_data){
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
