package com.hit_src.iot_terminal.object;

public class XMLRecord {
    public String name;
    public Integer localVersion;
    public Integer serverVersion;

    public XMLRecord(String name, Integer local, Integer server) {
        this.name=name;
        localVersion=local;
        serverVersion=server;
    }

    public XMLRecord(String s) {
        String[] ss=s.split(" ");
        name=ss[0];
        localVersion=null;
        serverVersion= Integer.valueOf(ss[1]);
    }

    public boolean localExists(){
        return localVersion!=null;
    }
    public boolean isOutDated(){
        return localVersion < serverVersion;
    }
}