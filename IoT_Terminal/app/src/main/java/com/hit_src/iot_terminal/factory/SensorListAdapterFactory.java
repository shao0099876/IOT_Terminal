package com.hit_src.iot_terminal.factory;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SensorListAdapterFactory {
    private static String[] from={"name","status"};
    private static int[] to={R.id.Overview_Sensor_ListviewLayout_name,R.id.Overview_Sensor_ListviewLayout_status};
    private static ArrayList<Integer> connectedList;
    private SensorListAdapterFactory(){}
    public static SimpleAdapter product(Context self, List<Sensor> dbList, ArrayList<Integer> statusList){
        connectedList=statusList;
        List<HashMap<String,Object>> data=new ArrayList<>();
        for (Sensor now : dbList) {
            data.add(draw(now));
        }
        return new SimpleAdapter(self,data, R.layout.overview_sensor_status_listview_layout,from,to);
    }

    private static HashMap<String, Object> draw(Sensor now) {
        HashMap<String,Object> res=new HashMap<>();
        res.put(from[0],now.toString());
        res.put(from[1],connectedList.contains(now.ID)?"  已连接":"  未连接");
        return res;
    }
}
