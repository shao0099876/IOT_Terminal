package com.hit_src.iot_terminal.object.sensortype;

public class Validity {
    public int min;
    public int max;

    public void setMin(String trim) {
        min=Integer.parseInt(trim);
    }

    public void setMax(String trim) {
        max=Integer.parseInt(trim);
    }

    public boolean judge(int res) {
        if(res<min || res>max){
            return false;
        }
        return true;
    }
}
