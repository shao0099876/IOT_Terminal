package com.hit_src.iot_terminal.object;

public class XMLRecord {
    private int status;
    public String name;
    public String localVersion;
    public String serverVersion;

    public XMLRecord(String name, int version, int version1) {
        this.name=name;
        localVersion= String.valueOf(version);
        serverVersion= String.valueOf(version1);
        if(version==version1){
            status=0;
        }
        else{
            status=1;
        }
    }

    public XMLRecord(String name, int version) {
        this.name=name;
        localVersion="-";
        serverVersion= String.valueOf(version);
        status=2;
    }
    public boolean localExists(){
        if(status==2){
            return false;
        }
        return true;
    }
    public boolean isOutDated(){
        if(status==1){
            return true;
        }
        return false;
    }
}