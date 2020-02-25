package com.hit_src.iot_terminal.object;

public class IP {
    private byte[] ipvalue;
    public IP(String s){
        ipvalue=new byte[4];
        String[] tmp=s.split(".");
        for(int i=0;i<4;i++){
            ipvalue[i]=Integer.valueOf(tmp[i]).byteValue();
        }
    }
}
