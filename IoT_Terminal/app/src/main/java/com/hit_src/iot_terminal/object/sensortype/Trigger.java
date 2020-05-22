package com.hit_src.iot_terminal.object.sensortype;

public class Trigger {
    public static final int BIGGER=1;
    public static final int SMALLER=2;
    public int type;
    public int value;
    public void setType(String trim) {
        if(trim.equals(">")){
            type=BIGGER;
        } else if(trim.equals("<")){
            type=SMALLER;
        }
    }

    public void setValue(String trim) {
        value=Integer.parseInt(trim);
    }

    public boolean trigger(int res) {
        if(type==BIGGER){
            return res>value;
        }
        if(type==SMALLER){
            return res<value;
        }
        return false;
    }
}
