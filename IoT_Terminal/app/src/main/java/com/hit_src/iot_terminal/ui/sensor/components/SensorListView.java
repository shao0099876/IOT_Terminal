package com.hit_src.iot_terminal.ui.sensor.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.SensorAdapter;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.sensor.info.SensorInfoFragment;

import java.util.ArrayList;
import java.util.Map;

public class SensorListView extends ListView {
    public SensorListView(Context context) {
        super(context);
        init();
    }

    public SensorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SensorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.Sensor_Detailed_Fragment, new SensorInfoFragment((Sensor) getSelectedItem()));
                transaction.commit();
            }
        });
        setDivider(getResources().getDrawable(R.drawable.sensorlist_divider_shape));
    }
    public void setData(final ArrayList<Sensor> sensors){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapter(makeAdapter(sensors));
            }
        });
    }
    private SimpleAdapter makeAdapter(ArrayList<Sensor> sensors){
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        for(Sensor i:sensors){
            arrayList.add(SensorAdapter.toListViewAdapter(i));
        }
        return new SimpleAdapter(getContext(),arrayList,R.layout.sensor_listview_layout,new String[]{"ID", "type", "status"}, new int[]{R.id.Sensor_ListView_No, R.id.Sensor_ListView_Type, R.id.Sensor_ListView_ConnectionStatus});
    }
}
