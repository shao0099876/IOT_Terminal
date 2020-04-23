package com.hit_src.iot_terminal.ui.sensor.add;

import android.widget.ArrayAdapter;

import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainActivity;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.sensortype.SensorType;


public class SensorAddViewModel extends ViewModel {
    MutableLiveData<ArrayAdapter<String>> sensorTypeLiveData=new MutableLiveData<>();
    public SensorAddViewModel(){
        String[] tmp=new String[GlobalVar.sensorTypes.size()];
        for(int i=0;i<tmp.length;i++){
            tmp[i]=GlobalVar.sensorTypes.get(i).name;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.self, R.layout.spinner_display_style, tmp);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sensorTypeLiveData.setValue(adapter);
        GlobalVar.sensorTypes.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<SensorType>>() {
            private void work(ObservableList<SensorType> sender){
                String[] tmp=new String[sender.size()];
                for(int i=0;i<tmp.length;i++){
                    tmp[i]=sender.get(i).name;
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.self, R.layout.spinner_display_style, tmp);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                sensorTypeLiveData.postValue(adapter);
            }
            @Override
            public void onChanged(ObservableList<SensorType> sender) {
                work(sender);
            }

            @Override
            public void onItemRangeChanged(ObservableList<SensorType> sender, int positionStart, int itemCount) {
                work(sender);
            }

            @Override
            public void onItemRangeInserted(ObservableList<SensorType> sender, int positionStart, int itemCount) {
                work(sender);
            }

            @Override
            public void onItemRangeMoved(ObservableList<SensorType> sender, int fromPosition, int toPosition, int itemCount) {
                work(sender);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<SensorType> sender, int positionStart, int itemCount) {
                work(sender);
            }
        });
    }
}
