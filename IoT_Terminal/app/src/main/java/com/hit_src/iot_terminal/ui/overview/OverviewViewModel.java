package com.hit_src.iot_terminal.ui.overview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
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
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Thread.sleep;

public class OverviewViewModel extends ViewModel {
    public MutableLiveData<Date> timeLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> sensorConnectedLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> sensorAmountLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> internetConnectionLiveData = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<String>> logLiveData = new MutableLiveData<>();

    public OverviewViewModel() {
        timeLiveData.setValue(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainApplication.self != null) {
                    timeLiveData.postValue(new Date());
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        sensorAmountLiveData.setValue(GlobalVar.sensorList.size());
        int connected = 0;
        for (Sensor i : GlobalVar.sensorList) {
            if (i.isConnected()) {
                connected += 1;
            }
        }
        sensorConnectedLiveData.setValue(connected);
        internetConnectionLiveData.setValue(InternetService.internetConnectionStatus.get());
        GlobalVar.sensorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
            void change(ObservableList<Sensor> sender) {
                sensorAmountLiveData.postValue(sender.size());
                int connected = 0;
                for (Sensor i : sender) {
                    if (i.isConnected()) {
                        connected += 1;
                    }
                }
                sensorConnectedLiveData.postValue(connected);
            }

            @Override
            public void onChanged(ObservableList<Sensor> sender) {
                change(sender);
            }

            @Override
            public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                change(sender);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                change(sender);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {
                change(sender);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                change(sender);
            }
        });
        InternetService.internetConnectionStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                internetConnectionLiveData.postValue(((ObservableBoolean) sender).get());
            }
        });
        logLiveData.setValue(new ArrayList<String>());

    }

    public static void addLogLiveData(String p) {
        ArrayList<String> tmp = logLiveData.getValue();
        if (tmp == null) {
            return;
        }
        tmp.add(p);
        logLiveData.postValue(tmp);
    }
}
