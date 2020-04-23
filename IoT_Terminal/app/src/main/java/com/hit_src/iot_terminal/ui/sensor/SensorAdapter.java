package com.hit_src.iot_terminal.ui.sensor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;

import java.util.List;

public class SensorAdapter extends BaseAdapter {
    private List<Sensor> sensorList;
    public SensorAdapter(List<Sensor> list){
        sensorList=list;
    }
    @Override
    public int getCount() {
        return sensorList.size();
    }

    @Override
    public Object getItem(int position) {
        return sensorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sensorList.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(MainApplication.self, R.layout.sensorlistview_layout,null);
        Sensor sensor=sensorList.get(position);
        ((TextView)(view.findViewById(R.id.SensorListView_ID))).setText(String.valueOf(sensor.getID()));
        SensorType sensorType = null;
        for(SensorType i:GlobalVar.sensorTypes){
            if(i.id==sensor.getType()){
                sensorType=i;
                break;
            }
        }
        ((TextView)(view.findViewById(R.id.SensorListView_Type))).setText(sensorType.name);
        String status;
        if(sensor.isConnected()){
            status="已连接";
        } else{
            status="未连接";
        }
        ((TextView)(view.findViewById(R.id.SensorListView_Status))).setText(status);
        return view;
    }
}
