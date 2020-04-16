package com.hit_src.iot_terminal;

import com.hit_src.iot_terminal.object.Sensor;

import java.util.HashMap;
import java.util.Map;

public class SensorAdapter {
    public static Map<String ,Object> toListViewAdapter(Sensor sensor){
        HashMap<String,Object> res=new HashMap<>();
        res.put("ID", sensor.getID());
        res.put("type", GlobalVar.sensorTypeMap.get(sensor.getType()).name);
        res.put("status", sensor.isConnected()?"已连接":"未连接");
        return res;
    }
}
