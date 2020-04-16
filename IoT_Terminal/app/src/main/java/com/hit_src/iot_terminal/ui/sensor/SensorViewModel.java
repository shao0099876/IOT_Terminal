package com.hit_src.iot_terminal.ui.sensor;

import android.os.RemoteException;
import android.util.Log;

import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.SensorService;

import java.util.ArrayList;

public class SensorViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Sensor>> sensorListLiveData=new MutableLiveData<>();
    public SensorViewModel(){
        sensorListLiveData.setValue((ArrayList<Sensor>) GlobalVar.sensorMap.values());
        GlobalVar.sensorMap.addOnMapChangedCallback(new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, Sensor>, Integer, Sensor>() {
            @Override
            public void onMapChanged(ObservableMap<Integer, Sensor> sender, Integer key) {
                sensorListLiveData.postValue((ArrayList<Sensor>) sender.values());
            }
        });
    }
}
