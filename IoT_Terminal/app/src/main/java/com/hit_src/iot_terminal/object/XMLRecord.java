package com.hit_src.iot_terminal.object;

public class XMLRecord {
    public String name;
    public Integer localVersion;
    public Integer remoteVersion;
    public Integer sensorTypeID;

    public XMLRecord(String name, Integer local, Integer server) {
        this.name=name;
        localVersion=local;
        remoteVersion=server;
    }

    public boolean localExists(){
        return localVersion!=null;
    }
    public boolean isOutDated(){
        return localVersion < remoteVersion;
    }

    public String getLocalVersion() {
        if(localVersion==null){
            return "-";
        }
        return String.valueOf(localVersion);
    }

    public String getRemoteVersion() {
        if(remoteVersion==null){
            return "-";
        }
        return String.valueOf(remoteVersion);
    }

    public void setSensorTypeID(int id) {
        sensorTypeID=id;
    }

    public void setRemoteVersion(int remoteVersion) {
        this.remoteVersion=remoteVersion;
    }

    public void setLocalVersion(int version) {
        this.localVersion=version;
    }
}