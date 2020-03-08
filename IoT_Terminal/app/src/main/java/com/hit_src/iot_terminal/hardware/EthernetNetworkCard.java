package com.hit_src.iot_terminal.hardware;

import java.io.UnsupportedEncodingException;

public class EthernetNetworkCard {
    static {
        System.loadLibrary("network-HAI");
    }
    private static EthernetNetworkCard self=new EthernetNetworkCard();
    private EthernetNetworkCard(){    }

    public static final int MAC=0;
    public static final int ADDR=1;
    public static final int BROADADDR=2;
    public static final int NETMASK=3;
    public static final int FLAGS=4;

    public static String getInfo(int METHOD){
        byte[] bytes=null;
        String name="eth0";
        try {
            switch (METHOD) {
                case MAC:
                    bytes = getMAC(name.getBytes("US-ASCII"));
                    break;
                case ADDR:
                    bytes = getADDR(name.getBytes("US-ASCII"));
                    break;
                case BROADADDR:
                    bytes = getBROADADDR(name.getBytes("US-ASCII"));
                    break;
                case NETMASK:
                    bytes = getNetMask(name.getBytes("US-ASCII"));
                    break;
                case FLAGS:
                    bytes = getFlags(name.getBytes("US-ASCII"));
                    break;
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String res=null;
        try{
            res=new String(bytes,"US-ASCII");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return res;
    }

    private static native byte[] getMAC(byte[] name);
    private static native byte[] getADDR(byte[] name);
    private static native byte[] getBROADADDR(byte[] name);
    private static native byte[] getNetMask(byte[] name);
    private static native byte[] getFlags(byte[] name);
    public static native byte[] getName(int index);

    public static int setInfo(int METHOD,String data,short flag){
        int res = 0;
        String name="eth0";
        try {
            switch (METHOD) {
                case MAC:
                    res=setMAC(name.getBytes("US-ASCII"),data.getBytes("US-ASCII"));
                    break;
                case ADDR:
                    res=setADDR(name.getBytes("US-ASCII"),data.getBytes("US-ASCII"));
                    break;
                case NETMASK:
                    res=setNetmask(name.getBytes("US-ASCII"),data.getBytes("US-ASCII"));
                    break;
                case FLAGS:
                    res=setFLAGS(name.getBytes("US-ASCII"),flag);
                    break;
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return res;
    }

    private static native int setMAC(byte[] name, byte[] mac);
    private static native int setADDR(byte[] name, byte[] addr);
    private static native int setNetmask(byte[] name, byte[] netmask);
    private static native int setFLAGS(byte[] name, short addr);

}
