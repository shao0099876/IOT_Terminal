package com.hit_src.iot_terminal.ui.overview;

import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.service.InternetService;
import com.hit_src.iot_terminal.service.SensorService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class OverviewViewModel extends ViewModel {
    public MutableLiveData<Integer> sensorConnectedLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> sensorAmountLiveData=new MutableLiveData<>();
    public MutableLiveData<Boolean> internetConnectionLiveData=new MutableLiveData<>();
    public MutableLiveData<String> logLiveData=new MutableLiveData<>();
    public OverviewViewModel(){
        sensorAmountLiveData.setValue(GlobalVar.sensorMap.size());
        int connected=0;
        for(Sensor i:GlobalVar.sensorMap.values()){
            if(i.isConnected()){
                connected+=1;
            }
        }
        sensorConnectedLiveData.setValue(connected);
        GlobalVar.sensorMap.addOnMapChangedCallback(new ObservableMap.OnMapChangedCallback<ObservableMap<Integer, Sensor>, Integer, Sensor>() {
            @Override
            public void onMapChanged(ObservableMap<Integer, Sensor> sender, Integer key) {
                sensorAmountLiveData.postValue(sender.size());
                int connected=0;
                for(Sensor i:sender.values()){
                    if(i.isConnected()){
                        connected+=1;
                    }
                }
                sensorConnectedLiveData.postValue(connected);
            }
        });

        internetConnectionLiveData.setValue(GlobalVar.internetStatus.get());
        GlobalVar.internetStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                internetConnectionLiveData.postValue(((ObservableBoolean)sender).get());
            }
        });
        StringBuilder sb=new StringBuilder();
        for(String i:GlobalVar.logArrayList){
            sb.append(i);
        }
        logLiveData.setValue(sb.toString());
        GlobalVar.logArrayList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> sender) {
                StringBuilder sb=new StringBuilder();
                for(String i:GlobalVar.logArrayList){
                    sb.append(i);
                }
                logLiveData.postValue(sb.toString());
            }

            @Override
            public void onItemRangeChanged(ObservableList<String> sender, int positionStart, int itemCount) {
                StringBuilder sb=new StringBuilder();
                for(String i:GlobalVar.logArrayList){
                    sb.append(i);
                }
                logLiveData.postValue(sb.toString());
            }

            @Override
            public void onItemRangeInserted(ObservableList<String> sender, int positionStart, int itemCount) {
                StringBuilder sb=new StringBuilder();
                for(String i:GlobalVar.logArrayList){
                    sb.append(i);
                }
                logLiveData.postValue(sb.toString());
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> sender, int fromPosition, int toPosition, int itemCount) {
                StringBuilder sb=new StringBuilder();
                for(String i:GlobalVar.logArrayList){
                    sb.append(i);
                }
                logLiveData.postValue(sb.toString());
            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> sender, int positionStart, int itemCount) {
                StringBuilder sb=new StringBuilder();
                for(String i:GlobalVar.logArrayList){
                    sb.append(i);
                }
                logLiveData.postValue(sb.toString());
            }
        });
    }
}
