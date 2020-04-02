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
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.DrawPoint;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.tools.ChartFormatTools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorInfoFragment extends Fragment {
    public Sensor sensor=null;
    private TextView sensorIDTextView;
    private TextView sensorTypeTextView;
    private Switch realtimeDataSwitch;
    private LineChart chart;

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
        sensorIDTextView=view.findViewById(R.id.Sensor_ID_TextView);
        sensorTypeTextView=view.findViewById(R.id.Sensor_Info_Type_TextView);
        realtimeDataSwitch=view.findViewById(R.id.Sensor_RealtimeData_Switch);
        chart=view.findViewById(R.id.Sensor_Draw_LineChart);

        realtimeDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            List<DrawPoint> pointList=null;
                            try {
                                pointList= MainApplication.dbService.getDrawPointbySensor(sensor.ID);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            ChartFormatTools.LineChartFormat(chart,pointList);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chart.invalidate();
                                }
                            });
                        }
                    };
                    timer.schedule(timerTask,10,1000);
                }
                else{
                    timer.cancel();
                    timerTask.cancel();
                    timer=null;
                    timerTask=null;
                }
            }
        });

        List<DrawPoint> pointList=null;
        try {
            pointList= MainApplication.dbService.getDrawPointbySensor(sensor.ID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ChartFormatTools.LineChartFormat(chart,pointList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorIDTextView.setText(String.valueOf(sensor.ID));
                sensorTypeTextView.setText(sensor.getType());
                realtimeDataSwitch.setChecked(false);
                chart.invalidate();
            }
        });


    }
}
