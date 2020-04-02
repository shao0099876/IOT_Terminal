package com.hit_src.iot_terminal.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;

public class Sensor implements Parcelable {
    public static String[] typeList={"床垫","血压","体温","血氧"};

    public int ID;
    public int type;
    public int addr;
    public boolean connected;

    public int sendLength=1;
    public int replyLength=2;

    public Sensor(int p1, int p2, int p3,boolean p4){
        ID=p1;
        type=p2;
        addr=p3;
        connected=p4;
    }

    public Sensor(Parcel in) {
        ID = in.readInt();
        type = in.readInt();
        addr=in.readInt();
    }

    public static final Creator<Sensor> CREATOR = new Creator<Sensor>() {
        @Override
        public Sensor createFromParcel(Parcel in) {
            return new Sensor(in);
        }

        @Override
        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };

    public Sensor(int id_res) {
        ID=id_res;
        type=-1;
        addr=-1;
        connected=false;
    }

    public int getID(){
        return ID;
    }
    public String getType(){
        return type==-1?"":typeList[type];
    }
    public boolean getConnectionStatus(){
        return connected;
    }


    public String toString(){
        return ID +"号" +typeList[type] +"传感器";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(type);
        dest.writeInt(addr);
    }
    public byte[] packageCMD() {
        ByteBuffer buffer=ByteBuffer.allocate(3+sendLength);
        buffer.put((byte)((addr>>=4)&15));
        buffer.put((byte)(addr&15));
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
