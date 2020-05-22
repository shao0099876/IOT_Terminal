package com.hit_src.testserver;

import com.hit_src.testserver.tools.BitOperation;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

import static com.hit_src.testserver.tools.BitOperation.*;

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
            byte[] reply=new byte[8];
            inputStream.read(reply);
            Log.d("-----Modbus数据包解析-----");
            byte[] crc_local=CRC(subArray(reply,0,reply.length-2));
            byte[] crc_remote=subArray(reply,reply.length-2,2);
            boolean crccheck=true;
            for(int i=0;i<2;i++){
                if(crc_local[i]!=crc_remote[i]){
                    crccheck=false;
                    break;
                }
            }
            if(crccheck){
                Log.d("CRC校验成功");
            }else{
                Log.d("CRC校验失败");
                return;
            }
            Log.d("从机地址："+reply[0]);
            Log.d("功能码："+reply[1]);
            Log.d("功能：读设备数量");
            Log.d("结果：设备数量为"+ BytestoInteger(subArray(reply,2,4)));
            Log.d("------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readDeviceInfo(int start, int len){
        CMD cmd=new CMD();
        cmd.addr=0x01;
        cmd.func=0x03;
        cmd.data=catArray(IntegertoBytes(start,2), IntegertoBytes(len,2));
        cmd.crc=CRC(cmd.toCRCRaw());
        try{
            outputStream.write(cmd.toByteArray());
            outputStream.flush();
            byte[] reply=new byte[4+12*len];
            inputStream.read(reply);
            Log.d("-----Modbus数据包解析-----");
            byte[] crc_local=CRC(subArray(reply,0,reply.length-2));
            byte[] crc_remote=subArray(reply,reply.length-2,2);
            boolean crccheck=true;
            for(int i=0;i<2;i++){
                if(crc_local[i]!=crc_remote[i]){
                    crccheck=false;
                    break;
                }
            }
            if(crccheck){
                Log.d("CRC校验成功");
            }else{
                Log.d("CRC校验失败");
                return;
            }
            Log.d("从机地址："+reply[0]);
            Log.d("功能码："+reply[1]);
            Log.d("功能：读设备信息");
            for(int i=0;i<len;i++){
                Log.d("-----");
                int l=2+12*i;
                int ID=BytestoInteger(subArray(reply,l,4));
                Log.d("设备ID："+ID);
                int typeID=BytestoInteger(subArray(reply,l+4,4));
                Log.d("设备类型："+typeID);
                int loraAddr=BytestoInteger(subArray(reply,l+8,2));
                Log.d("LoRa地址："+loraAddr);
                boolean enabledStatus= BytestoInteger(subArray(reply, l + 10, 1)) == 1;
                Log.d("设备启用状态："+(enabledStatus?"已启用":"未启用"));
                boolean connection=BytestoInteger(subArray(reply,l+11,1))==1;
                Log.d("设备连接状态："+(connection?"已连接":"未连接"));
            }
            Log.d("------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readDeviceDataCnt(){
        CMD cmd=new CMD();
        cmd.addr=0x01;
        cmd.func=0x04;
        cmd.data=catArray(IntegertoBytes(0xFFFF,2), IntegertoBytes(1,2));
        cmd.crc=CRC(cmd.toCRCRaw());
        try{
            outputStream.write(cmd.toByteArray());
            outputStream.flush();
            byte[] reply=new byte[4+4];
            inputStream.read(reply);
            Log.d("-----Modbus数据包解析-----");
            byte[] crc_local=CRC(subArray(reply,0,reply.length-2));
            byte[] crc_remote=subArray(reply,reply.length-2,2);
            boolean crccheck=true;
            for(int i=0;i<2;i++){
                if(crc_local[i]!=crc_remote[i]){
                    crccheck=false;
                    break;
                }
            }
            if(crccheck){
                Log.d("CRC校验成功");
            }else{
                Log.d("CRC校验失败");
                return;
            }
            Log.d("从机地址："+reply[0]);
            Log.d("功能码："+reply[1]);
            Log.d("功能：读设备数据数量");
            Log.d("设备数据条数："+BytestoInteger(subArray(reply,2,4)));
            Log.d("------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readDeviceData(int len){
        CMD cmd=new CMD();
        cmd.addr=0x01;
        cmd.func=0x04;
        cmd.data=catArray(IntegertoBytes(0x0,2), IntegertoBytes(len,2));
        cmd.crc=CRC(cmd.toCRCRaw());
        try{
            outputStream.write(cmd.toByteArray());
            outputStream.flush();
            byte[] reply=new byte[4+16*len];
            inputStream.read(reply);
            Log.d("-----Modbus数据包解析-----");
            byte[] crc_local=CRC(subArray(reply,0,reply.length-2));
            byte[] crc_remote=subArray(reply,reply.length-2,2);
            boolean crccheck=true;
            for(int i=0;i<2;i++){
                if(crc_local[i]!=crc_remote[i]){
                    crccheck=false;
                    break;
                }
            }
            if(crccheck){
                Log.d("CRC校验成功");
            }else{
                Log.d("CRC校验失败");
                return;
            }
            Log.d("从机地址："+reply[0]);
            Log.d("功能码："+reply[1]);
            Log.d("功能：读设备数据");
            for(int i=0;i<len;i++){
                Log.d("-----");
                int l=2+16*i;
                Log.d("设备ID："+BytestoInteger(subArray(reply,l,4)));
                Log.d("数据值"+BytestoInteger(subArray(reply,l+4,4)));
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                Log.d("时间"+simpleDateFormat.format(new Date(BytestoLong(subArray(reply,l+8,8)))));
            }
            Log.d("------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private byte[] subArray(byte[] p,int start,int len){
        byte[] res=new byte[len];
        for(int i=0;i<len;i++){
            res[i]=p[start+i];
        }
        return res;
    }
    private byte[] catArray(byte[] p,byte[] p1){
        byte[] res=new byte[p.length+p1.length];
        for(int i=0;i<p.length;i++){
            res[i]=p[i];
        }
        for(int i=0;i<p1.length;i++){
            res[i+p.length]=p1[i];
        }
        return res;
    }
    private byte[] CRC(byte[] raw_data){
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

}
