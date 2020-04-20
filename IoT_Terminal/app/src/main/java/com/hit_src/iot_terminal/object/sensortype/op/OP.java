package com.hit_src.iot_terminal.object.sensortype.op;

public abstract class OP {
    boolean isMemory;
    final int value;
    OP(String trim){
        String tmp;
        if(trim.charAt(0)=='['){
            isMemory=true;
            tmp=trim.substring(1,trim.length()-1);
        }
        else{
            tmp=trim;
        }
        value=Integer.parseInt(tmp);
    }
    public abstract int calculate(int reg,byte[] source);
}
