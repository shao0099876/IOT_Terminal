package com.hit_src.iot_terminal.ui.sensor.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.ui.sensor.SensorInfoFragment;

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
    }
}
