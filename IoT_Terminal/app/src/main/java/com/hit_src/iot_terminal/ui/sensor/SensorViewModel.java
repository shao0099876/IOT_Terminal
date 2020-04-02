package com.hit_src.iot_terminal.ui.sensor;

import android.os.RemoteException;

import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.SensorService;

import java.util.ArrayList;

public class SensorViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Sensor>> sensorListLiveData=new MutableLiveData<>();
    public SensorViewModel(){
        try {
            sensorListLiveData.setValue((ArrayList<Sensor>) MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        SensorService.sensorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
            @Override
            public void onChanged(ObservableList<Sensor> sender) {
            }

            @Override
            public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                sensorListLiveData.postValue(new ArrayList<>(sender.subList(0, sender.size())));
            }

            @Override
            public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {

            }
        });
    }
}
