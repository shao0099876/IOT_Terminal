package com.hit_src.iot_terminal.ui.sensor;

import android.os.Bundle;
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
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.DataRecord;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.DatabaseService;
import com.hit_src.iot_terminal.service.SensorService;
import com.hit_src.iot_terminal.tools.DataChart;

import static java.lang.Thread.sleep;

public class SensorInfoFragment extends Fragment {
    public Sensor sensor = null;
    private Thread thread = null;
    private TextView sensorIDTextView;
    private TextView sensorTypeTextView;
    private TextView sensorLoraAddrTextView;
    private Switch enabledSwitch;
    private Switch realtimeDataSwitch;
    private DataChart chart = new DataChart();

    public SensorInfoFragment() {
    }

    public SensorInfoFragment(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensorinfo, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        assert view != null;
        sensorIDTextView = view.findViewById(R.id.Sensor_ID_TextView);
        sensorTypeTextView = view.findViewById(R.id.Sensor_Info_Type_TextView);
        sensorLoraAddrTextView = view.findViewById(R.id.Sensor_Info_LoraAddr_TextView);
        enabledSwitch = view.findViewById(R.id.Sensor_Enabled_Switch);
        realtimeDataSwitch = view.findViewById(R.id.Sensor_RealtimeData_Switch);
        chart.setComponent((LineChart) view.findViewById(R.id.Sensor_Draw_LineChart));
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensorIDTextView.setText(String.valueOf(sensor.getID()));
                sensorTypeTextView.setText(GlobalVar.sensorTypeHashMap.get(sensor.getType()).name);
                sensorLoraAddrTextView.setText(String.valueOf(sensor.getLoraAddr()));
                ;
                enabledSwitch.setChecked(sensor.isEnabled());
                realtimeDataSwitch.setChecked(false);
                chart.setData(DatabaseService.getInstance().getDataRecordsbySensorID(sensor.getID()));
            }
        });
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sensor.setEnabled(isChecked);
                if (!isChecked) {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realtimeDataSwitch.setChecked(false);
                            realtimeDataSwitch.setClickable(false);
                        }
                    });
                } else {
                    MainActivity.self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realtimeDataSwitch.setClickable(true);
                        }
                    });
                }
                GlobalVar.updateSensor(sensor);
            }
        });
        realtimeDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            chart.setData(DatabaseService.getInstance().getDataRecordsbySensorID(sensor.getID()));
                            DataRecord dataRecord = SensorService.getRealtimeData(sensor.getID());
                            if (dataRecord != null) {
                                chart.addData(dataRecord);
                                MainActivity.self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        chart.invalidate(true);
                                    }
                                });
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                } else {
                    thread.interrupt();
                }
            }
        });
    }
}
