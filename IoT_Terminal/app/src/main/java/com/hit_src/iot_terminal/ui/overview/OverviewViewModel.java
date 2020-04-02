package com.hit_src.iot_terminal.ui.overview;

import android.os.RemoteException;
import android.provider.ContactsContract;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.service.InternetService;
import com.hit_src.iot_terminal.service.SensorService;

import java.util.ArrayList;

public class OverviewViewModel extends ViewModel {
    public MutableLiveData<Integer> sensorConnectedLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> sensorAmountLiveData=new MutableLiveData<>();
    public MutableLiveData<Boolean> internetConnectionLiveData=new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> logLiveData=new MutableLiveData<>();
    public OverviewViewModel(){
        sensorConnectedLiveData.setValue(0);
        try {
            sensorAmountLiveData.setValue(MainApplication.dbService.getSensorAmount());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        internetConnectionLiveData.setValue(InternetService.internetConnectionStatus.get());

        SensorService.sensorConnectedAmount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                sensorConnectedLiveData.postValue(((ObservableInt)sender).get());
            }
        });
        SensorService.sensorAmount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                sensorAmountLiveData.postValue(((ObservableInt)sender).get());
            }
        });
        InternetService.internetConnectionStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                internetConnectionLiveData.postValue(((ObservableBoolean)sender).get());
            }
        });
        logLiveData.setValue(new ArrayList<String>());

    }
    public void addLogLiveData(String p){
        ArrayList<String> tmp=logLiveData.getValue();
        tmp.add(p);
        logLiveData.postValue(tmp);
    }
}
