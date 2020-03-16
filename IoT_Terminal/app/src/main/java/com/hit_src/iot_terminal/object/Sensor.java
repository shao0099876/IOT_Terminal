package com.hit_src.iot_terminal.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;

public class Sensor implements Parcelable {
    public static String[] typeList={"床垫","血压","体温","血氧"};

    public int ID;
    public int type;
    public int addr;
    public int sendLength=1;
    public int replyLength=2;

    public Sensor(int p1, int p2, int p3){
        ID=p1;
        type=p2;
        addr=p3;
    }

    protected Sensor(Parcel in) {
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
        res=raw_data[0];
        res<<=8;
        res&=raw_data[1];
        return res;
    }
}
