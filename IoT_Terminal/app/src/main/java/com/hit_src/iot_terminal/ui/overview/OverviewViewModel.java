package com.hit_src.iot_terminal.ui.overview;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit_src.iot_terminal.GlobalVar;
import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Thread.sleep;

public class OverviewViewModel extends ViewModel {
    MutableLiveData<String> timeLiveData=new MutableLiveData<>();
    MutableLiveData<String> dateLiveData=new MutableLiveData<>();
    MutableLiveData<Integer> sensorStatusColorLiveData=new MutableLiveData<>();
    MutableLiveData<String> sensorStatusTextLiveData=new MutableLiveData<>();
    MutableLiveData<Integer> internetStatusColorLiveData=new MutableLiveData<>();
    MutableLiveData<String> internetStatusTextLiveData=new MutableLiveData<>();
    MutableLiveData<String> logLiveData=new MutableLiveData<>();
    public OverviewViewModel(){
        //日期时间
        {
            //初始化
            timeLiveData.setValue("");
            dateLiveData.setValue("");
            //数据来源
            new Thread(new Runnable(){
                @Override
                public void run() {
                    while (MainApplication.self != null) {
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            timeLiveData.postValue(simpleDateFormat.format(new Date()));
                        }
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            dateLiveData.postValue(simpleDateFormat.format(new Date()));
                        }

                        try {
                            sleep(999);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        //传感器状态
        {
            //初始化
            sensorStatusTextLiveData.setValue("0/0");
            sensorStatusColorLiveData.setValue(R.color.Overview_Status_Green);
            //数据来源
            GlobalVar.sensors.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
                private void work(ObservableList<Sensor> sender){
                    int amount=sender.size();
                    int connected=0;
                    for(Sensor i:sender){
                        if(i.isConnected()){
                            connected+=1;
                        }
                    }
                    int color;
                    if(connected==amount){
                        color=R.color.Overview_Status_Green;
                    }
                    else if(connected==0){
                        color=R.color.Overview_Status_Red;
                    }
                    else {
                        color=R.color.Overview_Status_Yellow;
                    }
                    String stringBuilder = connected +
                            "/" +
                            amount;
                    sensorStatusTextLiveData.postValue(stringBuilder);
                    sensorStatusColorLiveData.postValue(color);
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
        //网络状态
        {
            //初始化
            internetStatusTextLiveData.setValue("未连接");
            internetStatusColorLiveData.setValue(R.color.Overview_Status_Red);
            //数据来源
            GlobalVar.internetStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    boolean status=((ObservableBoolean)sender).get();
                    if(status){
                        internetStatusColorLiveData.postValue(R.color.Overview_Status_Green);
                        internetStatusTextLiveData.postValue("已连接");
                    } else{
                        internetStatusColorLiveData.postValue(R.color.Overview_Status_Red);
                        internetStatusTextLiveData.postValue("未连接");
                    }
                }
            });
        }
        //日志
        {
            StringBuilder sb=new StringBuilder();
            for(String i:GlobalVar.logArrayList){
                sb.append(i);
            }
            logLiveData.setValue(sb.toString());
            GlobalVar.logArrayList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
                private void work(){
                    StringBuilder sb=new StringBuilder();
                    for(String i:GlobalVar.logArrayList){
                        sb.append(i);
                    }
                    logLiveData.postValue(sb.toString());
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
