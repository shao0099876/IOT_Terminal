package com.hit_src.iot_terminal.ui.sensor;

import androidx.databinding.ObservableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;

public class SensorViewModel extends ViewModel {
    public final MutableLiveData<ArrayList<Sensor>> sensorListLiveData=new MutableLiveData<>();
    public SensorViewModel(){
        sensorListLiveData.setValue(new ArrayList<Sensor>(GlobalVar.sensors.subList(0,GlobalVar.sensors.size())));
        /*GlobalVar.sensorMap.addOnMapChangedCallback(new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, Sensor>, Integer, Sensor>() {
            @Override
            public void onMapChanged(ObservableMap<Integer, Sensor> sender, Integer key) {
                sensorListLiveData.postValue(new ArrayList<>(sender.values()));
            }
        });*/
    }
}
