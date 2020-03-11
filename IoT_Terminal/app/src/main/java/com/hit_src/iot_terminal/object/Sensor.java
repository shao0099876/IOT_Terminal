package com.hit_src.iot_terminal.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Sensor implements Parcelable {
    public static String[] typeList={"床垫","血压","体温","血氧"};

    public int ID;
    public int type;

    public Sensor(int p1, int p2){
        ID=p1;
        type=p2;
    }

    protected Sensor(Parcel in) {
        ID = in.readInt();
        type = in.readInt();
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
    }
}
