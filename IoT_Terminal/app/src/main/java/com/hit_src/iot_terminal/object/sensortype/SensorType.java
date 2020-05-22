package com.hit_src.iot_terminal.object.sensortype;

import java.util.ArrayList;

public class SensorType {
    public int id;
    public String name;
    public Datatype data;
    public Send send;
    public Receive recv;
    public Validity validity;
    public ArrayList<Alert> alerts;

    public void setID(String trim) {
        id = Integer.parseInt(trim);
    }

    public void setName(String trim) {
        name = trim;
    }

    public void setDataType(Datatype data) {
        this.data = data;
    }

    public void setSend(Send send) {
        this.send = send;
    }

    public void setReceive(Receive receive) {
        recv = receive;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public void setAlerts(ArrayList<Alert> alerts) {
        this.alerts = alerts;
    }
}
