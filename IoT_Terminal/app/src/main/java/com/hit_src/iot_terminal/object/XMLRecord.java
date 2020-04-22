package com.hit_src.iot_terminal.object;

public class XMLRecord {
    public String name;
    public Integer localVersion;
    public Integer serverVersion;
    public Integer sensorTypeID;

    public XMLRecord(String name, Integer local, Integer server) {
        this.name=name;
        localVersion=local;
        serverVersion=server;
    }

    public boolean localExists(){
        return localVersion!=null;
    }
    public boolean isOutDated(){
        return localVersion < serverVersion;
    }

    public String getLocalVersion() {
        if(localVersion==null){
            return "-";
        }
        return String.valueOf(localVersion);
    }

    public String getServerVersion() {
        if(serverVersion==null){
            return "-";
        }
        return String.valueOf(serverVersion);
    }

    public void setSensorTypeID(int id) {
        sensorTypeID=id;
    }

    public void setServerVersion(int serverVersion) {
        this.serverVersion=serverVersion;
    }

    public void setLocalVersion(int version) {
        this.localVersion=version;
    }
}