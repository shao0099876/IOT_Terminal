package com.hit_src.iot_terminal.ui.overview;

import android.util.Pair;

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
    public MutableLiveData<Pair<Integer,Integer>> sensorStatusLiveData=new MutableLiveData<>();
    public MutableLiveData<Boolean> internetConnectionLiveData = new MutableLiveData<>();
    public static MutableLiveData<String> logLiveData = new MutableLiveData<>();

    public OverviewViewModel() {
        {
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
        }
        {
            sensorStatusLiveData.setValue(new Pair<>(GlobalVar.getSensorConnectedAmount(), GlobalVar.sensorList.size()));
            GlobalVar.sensorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
                private void work(){
                    sensorStatusLiveData.postValue(new Pair<>(GlobalVar.getSensorConnectedAmount(), GlobalVar.sensorList.size()));
                }
                @Override
                public void onChanged(ObservableList<Sensor> sender) {
                    work();
                }

                @Override
                public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    work();
                }
            });
        }

        internetConnectionLiveData.setValue(InternetService.internetConnectionStatus.get());
        InternetService.internetConnectionStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                internetConnectionLiveData.postValue(((ObservableBoolean) sender).get());
            }
        });
        {
            logLiveData.setValue(GlobalVar.getLog());
            GlobalVar.logList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
                private void work(){
                    logLiveData.postValue(GlobalVar.getLog());
                }
                @Override
                public void onChanged(ObservableList<String> sender) {
                    work();
                }

                @Override
                public void onItemRangeChanged(ObservableList<String> sender, int positionStart, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeInserted(ObservableList<String> sender, int positionStart, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeMoved(ObservableList<String> sender, int fromPosition, int toPosition, int itemCount) {
                    work();
                }

                @Override
                public void onItemRangeRemoved(ObservableList<String> sender, int positionStart, int itemCount) {
                    work();
                }
            });
        }

    }
}
