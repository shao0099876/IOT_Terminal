package com.hit_src.iot_terminal.hardware;

public class EthernetNetworkCard {
    private static EthernetNetworkCard self=new EthernetNetworkCard();
    private EthernetNetworkCard(){
    }
    public static native String getnetworkConfig();
}
