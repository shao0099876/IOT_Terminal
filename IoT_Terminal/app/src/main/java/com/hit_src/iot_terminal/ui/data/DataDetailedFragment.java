package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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
import com.hit_src.iot_terminal.object.sensortype.Datatype;
import com.hit_src.iot_terminal.service.DatabaseService;
import com.hit_src.iot_terminal.service.SensorService;
import com.hit_src.iot_terminal.tools.DataChart;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class DataDetailedFragment extends Fragment {
    private Datatype datatype = null;
    private Thread thread;
    private Switch realtimeSwitch;
    private DataChart chart = new DataChart();

    public DataDetailedFragment() {
    }

    public DataDetailedFragment(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datadetailed, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        assert view != null;
        realtimeSwitch = view.findViewById(R.id.Data_Realtime_Switch);
        chart.setComponent((LineChart) view.findViewById(R.id.Data_Draw_LineChart));
        chart.setData(DatabaseService.getInstance().getDataRecordsbyDatatypeName(datatype.name));
        MainActivity.self.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chart.invalidate(false);
                realtimeSwitch.setChecked(false);
            }
        });
        realtimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chart.setData(DatabaseService.getInstance().getDataRecordsbyDatatypeName(datatype.name));
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (MainApplication.self != null) {
                                ArrayList<Sensor> sensors = GlobalVar.getSensorListbyDatatype(datatype.name);
                                DataRecord dataRecord = SensorService.getRealtimeData(sensors);
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
