package com.hit_src.iot_terminal.object;

import android.os.Parcel;
import android.os.Parcelable;

public class DrawPoint implements Parcelable {
    public long time;
    public int data;
    public DrawPoint(long p1,int p2){
        time=p1;
        data=p2;
    }

    protected DrawPoint(Parcel in) {
        time = in.readLong();
        data = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeInt(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DrawPoint> CREATOR = new Creator<DrawPoint>() {
        @Override
        public DrawPoint createFromParcel(Parcel in) {
            return new DrawPoint(in);
        }

        @Override
        public DrawPoint[] newArray(int size) {
            return new DrawPoint[size];
        }
    };
}
