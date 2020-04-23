package com.hit_src.iot_terminal.ui.sensor;

import android.widget.BaseAdapter;

import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.ArrayList;

public class SensorViewModel extends ViewModel {
    MutableLiveData<BaseAdapter> sensorListLiveData=new MutableLiveData<>();
    public SensorViewModel(){
        //传感器列表
        {
            sensorListLiveData.setValue(new SensorAdapter(GlobalVar.sensors));
            GlobalVar.sensors.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
                private void work(ObservableList<Sensor> sender){
                    sensorListLiveData.postValue(new SensorAdapter(sender));
                }
                @Override
                public void onChanged(ObservableList<Sensor> sender) {
                    work(sender);
                }

                @Override
                public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work(sender);
                }
            });
        }
    }
}
