package com.hit_src.iot_terminal.ui.sensor;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.service.SensorService;
import com.hit_src.iot_terminal.tools.DataChart;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorInfoFragment extends Fragment {
    private Sensor sensor=null;
    private TextView sensorIDTextView;
    private TextView sensorTypeTextView;
    private TextView sensorLoraAddrTextView;
    private Switch enabledSwitch;
    private Switch realtimeDataSwitch;
    private final DataChart chart=new DataChart();

    private Timer timer;
    private TimerTask timerTask;

    public SensorInfoFragment(){}
    public SensorInfoFragment(Sensor sensor){
        this.sensor=sensor;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensorinfo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();

        View view=getView();
        assert view != null;
        sensorIDTextView=view.findViewById(R.id.Sensor_ID_TextView);
        sensorTypeTextView=view.findViewById(R.id.Sensor_Info_Type_TextView);
        sensorLoraAddrTextView=view.findViewById(R.id.Sensor_Info_LoraAddr_TextView);
        enabledSwitch=view.findViewById(R.id.Sensor_Enabled_Switch);
        realtimeDataSwitch=view.findViewById(R.id.Sensor_RealtimeData_Switch);
        chart.setComponent((LineChart) view.findViewById(R.id.Sensor_Draw_LineChart));

        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==sensor.isEnabled()){
                    return;
                }
                sensor.setEnabled(isChecked);
                if(!isChecked){
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realtimeDataSwitch.setChecked(false);
                            realtimeDataSwitch.setEnabled(false);
                        }
                    });
                }
                else {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realtimeDataSwitch.setEnabled(true);
                        }
                    });
                }
                try {
                    MainApplication.dbService.updateSensor(sensor.getID(),sensor.getType(),sensor.getLoraAddr(),isChecked);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        realtimeDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    List<DataRecord> dataList=null;
                    try {
                        dataList= MainApplication.dbService.getDrawPointbySensor(sensor.getID());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    assert dataList != null;
                    chart.setData(dataList);
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            DataRecord dataRecord= SensorService.getRealtimeData(sensor.getID());
                            if(dataRecord==null){
                                return;
                            }
                            chart.addData(dataRecord);
                            try {
                                MainActivity.self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        chart.invalidate(true);
                                    }
                                });
                            } catch (NullPointerException e){
                                timer.cancel();
                                timerTask.cancel();
                                timer=null;
                                timerTask=null;
                            }

                        }
                    };
                    timer.schedule(timerTask,0,1000);
                }
                else{
                    timer.cancel();
                    timerTask.cancel();
                    timer=null;
                    timerTask=null;
                }
            }
        });

        List<DataRecord> dataList=null;
        try {
            dataList= MainApplication.dbService.getDrawPointbySensor(sensor.getID());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assert dataList != null;
        chart.setData(dataList);
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorIDTextView.setText(String.valueOf(sensor.getID()));
                SensorType sensorType=null;
                for(SensorType i:GlobalVar.sensorTypes){
                    if(i.id==sensor.getType()){
                        sensorType=i;
                        break;
                    }
                }
                assert sensorType != null;
                sensorTypeTextView.setText(sensorType.name);
                sensorLoraAddrTextView.setText(String.valueOf(sensor.getLoraAddr()));
                enabledSwitch.setChecked(sensor.isEnabled());
                realtimeDataSwitch.setChecked(false);
                chart.invalidate(false);
            }
        });


    }
}
