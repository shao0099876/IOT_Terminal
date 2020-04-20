package com.hit_src.iot_terminal.ui.data;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
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
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.service.SensorService;
import com.hit_src.iot_terminal.tools.DataChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class DataDetailedFragment extends Fragment {
    private String Datatype=null;
    private Timer timer;
    private TimerTask timerTask;
    private ArrayList<Sensor> sensors;

    private Switch realtimeSwitch;
    private final DataChart chart=new DataChart();

    public DataDetailedFragment(){}
    DataDetailedFragment(String datatype){
        Datatype=datatype;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datadetailed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        View view=getView();
        assert view != null;
        realtimeSwitch=view.findViewById(R.id.Data_Realtime_Switch);
        chart.setComponent((LineChart) view.findViewById(R.id.Data_Draw_LineChart));

        realtimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<DataRecord> dataRecords=new ArrayList<>();
                    for(Sensor i:sensors){
                        try {
                            dataRecords.addAll((ArrayList<DataRecord>) MainApplication.dbService.getDrawPointbySensor(i.getID()));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    chart.setData(dataRecords);
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            DataRecord dataRecord= SensorService.getRealtimeData(sensors);
                            if(dataRecord==null){
                                return;
                            }
                            chart.addData(dataRecord);
                            try{
                                MainActivity.self.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        chart.invalidate(true);
                                    }
                                });
                            }catch (NullPointerException e){
                                timer.cancel();
                                timerTask.cancel();
                                timer=null;
                                timerTask=null;
                            }

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

        //根据Datatype找Sensor
        List<Sensor> sensorArrayList= (List<Sensor>) GlobalVar.sensorMap.values();
        sensors=new ArrayList<>();
        for(Sensor i:sensorArrayList){
            SensorType sensorType= GlobalVar.sensorTypeMap.get(i.getType());
            assert sensorType != null;
            if(sensorType.data.name.equals(Datatype)){
                sensors.add(i);
            }
        }
        ArrayList<DataRecord> dataRecords=new ArrayList<>();
        for(Sensor i:sensors){
            try {
                ArrayList<DataRecord> dataRecordArrayList= (ArrayList<DataRecord>) MainApplication.dbService.getDrawPointbySensor(i.getID());
                for(DataRecord j:dataRecordArrayList){
                    Log.e("SRCDEBUG", String.valueOf(j));
                }
                dataRecords.addAll((ArrayList<DataRecord>) MainApplication.dbService.getDrawPointbySensor(i.getID()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        chart.setData(dataRecords);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                realtimeSwitch.setChecked(false);
                chart.invalidate(false);
            }
        });
    }
}
