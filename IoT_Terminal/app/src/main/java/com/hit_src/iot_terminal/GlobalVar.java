package com.hit_src.iot_terminal;

import android.os.RemoteException;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;

import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.object.XMLRecord;
import com.hit_src.iot_terminal.object.sensortype.SensorType;
import com.hit_src.iot_terminal.tools.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class GlobalVar {
    public static ObservableArrayList<XMLRecord> xmlRecords=new ObservableArrayList<>();
    public static ObservableArrayList<SensorType> sensorTypes=new ObservableArrayList<>();
    public static ObservableArrayList<Sensor> sensors=new ObservableArrayList<>();
    public static ObservableBoolean internetStatus=new ObservableBoolean();
    public static ObservableArrayList<String> logArrayList=new ObservableArrayList<>();
    private static GlobalVar self=null;
    private GlobalVar(){
        //xmlRecords
        {
            xmlRecords.addAll(PackageManager.buildXMLRecords());
            xmlRecords.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<XMLRecord>>() {
                private void work(ObservableList<XMLRecord> sender){
                    PackageManager.write(sender);
                    ArrayList<SensorType> tmp=PackageManager.buildSensorTypes();
                    if(tmp.size()>sensorTypes.size()){
                        for(SensorType i:tmp){
                            boolean exists=false;
                            for(int j=0;j<sensorTypes.size();j++){
                                SensorType now=sensorTypes.get(j);
                                if(now.id==i.id){
                                    sensorTypes.set(j,i);
                                    exists=true;
                                    break;
                                }
                            }
                            if(!exists){
                                sensorTypes.add(i);
                            }
                        }
                    } else{
                        int i=0;
                        for(SensorType now:sensorTypes){
                            boolean exists=false;
                            for(SensorType j:tmp){
                                if(now.id==j.id){
                                    sensorTypes.set(i,j);
                                    exists=true;
                                    break;
                                }
                            }
                            if(!exists){
                                sensorTypes.remove(i);
                            }
                            i+=1;
                        }
                    }

                }
                @Override
                public void onChanged(ObservableList<XMLRecord> sender) {
                    work(sender);
                }

                @Override
                public void onItemRangeChanged(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeInserted(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeMoved(ObservableList<XMLRecord> sender, int fromPosition, int toPosition, int itemCount) {
                    work(sender);
                }

                @Override
                public void onItemRangeRemoved(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    work(sender);
                }
            });
        }
        //sensorTypes
        sensorTypes.addAll(PackageManager.buildSensorTypes());
        //sensors
        try {
            sensors.addAll(MainApplication.dbService.getSensorList());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //internetStatus
        internetStatus.set(false);
    }
    public static void addLogLiveData(String p){
        if(logArrayList.size()>10){
            logArrayList.remove(0);
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss >", Locale.CHINA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        logArrayList.add(simpleDateFormat.format(new Date())+p+"\n");
    }

    public static void createInstance() {
        if(self==null){
            self=new GlobalVar();
        }
    }
}
