package com.hit_src.iot_terminal;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.util.HashMap;
import java.util.Map;

public class SensorAdapter {
    public static Map<String ,Object> toListViewAdapter(Sensor sensor){
        HashMap<String,Object> res=new HashMap<>();
        res.put("ID", sensor.getID());

        SensorType sensorType=null;
        for(SensorType i:GlobalVar.sensorTypes){
            if(i.id==sensor.getType()){
                sensorType=i;
                break;
            }
        }
        assert sensorType != null;
        res.put("type", sensorType.name);
        res.put("status", sensor.isConnected()?"已连接":"未连接");
        return res;
    }
}
