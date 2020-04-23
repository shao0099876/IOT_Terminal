package com.hit_src.iot_terminal;

import android.os.RemoteException;

import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableMap;

import com.github.mikephil.charting.utils.FSize;
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
import java.util.Set;
import java.util.TimeZone;

import javax.security.auth.callback.PasswordCallback;

public class GlobalVar {
    public static XMLRecord[] lastXMLRecords=new XMLRecord[0];
    public static ObservableArrayList<XMLRecord> xmlRecords=new ObservableArrayList<>();
    public static ObservableArrayList<SensorType> sensorTypes=new ObservableArrayList<>();
    public static ObservableArrayList<Sensor> sensors=new ObservableArrayList<>();
    public static Sensor[] lastSensors=new Sensor[0];
    public static ObservableBoolean internetStatus=new ObservableBoolean();
    public static ObservableArrayList<String> logArrayList=new ObservableArrayList<>();
    private static GlobalVar self=null;
    private GlobalVar(){
        //xmlRecords
        {
            xmlRecords.addAll(PackageManager.buildXMLRecords());
            lastXMLRecords=xmlRecords.toArray(lastXMLRecords);
            xmlRecords.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<XMLRecord>>() {
                @Override
                public void onChanged(ObservableList<XMLRecord> sender) {

                }

                @Override
                public void onItemRangeChanged(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {
                    PackageManager.write(sender);
                    for(int i=positionStart;i<positionStart+itemCount;i++){
                        XMLRecord last=lastXMLRecords[i];
                        XMLRecord now=sender.get(i);
                        if(last.localVersion==null&&now.localVersion!=null){
                            //PackageManager.
                        }

                    }
                }

                @Override
                public void onItemRangeInserted(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {

                }

                @Override
                public void onItemRangeMoved(ObservableList<XMLRecord> sender, int fromPosition, int toPosition, int itemCount) {

                }

                @Override
                public void onItemRangeRemoved(ObservableList<XMLRecord> sender, int positionStart, int itemCount) {

                }
            });
        }
        //sensorTypes
        sensorTypes.addAll(PackageManager.buildSensorTypes());
        sensorTypes.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<SensorType>>() {
            private void work(ObservableList<SensorType> sender){
                for(Sensor i:sensors){
                    boolean exists=false;
                    for(SensorType j:sender){
                        if(i.getType()==j.id){
                            exists=true;
                            break;
                        }
                    }
                    if(!exists){
                        sensors.remove(i);
                    }
                }
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
        //sensors
        try {
            sensors.addAll(MainApplication.dbService.getSensorList());
            lastSensors= sensors.toArray(lastSensors);
            sensors.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Sensor>>() {
                @Override
                public void onChanged(ObservableList<Sensor> sender) {
                    lastSensors= sensors.toArray(lastSensors);
                }

                @Override
                public void onItemRangeChanged(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    lastSensors= sensors.toArray(lastSensors);
                    for(int i=positionStart;i<positionStart+itemCount;i++){
                        Sensor now=sender.get(i);
                        try {
                            MainApplication.dbService.updateSensor(now.getID(),now.getType(),now.getLoraAddr(),now.isEnabled());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onItemRangeInserted(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    lastSensors= sensors.toArray(lastSensors);
                    for(int i=positionStart;i<positionStart+itemCount;i++){
                        Sensor now=sender.get(i);
                        try {
                            MainApplication.dbService.addSensor(now.getID(),now.getType(),now.getLoraAddr());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onItemRangeMoved(ObservableList<Sensor> sender, int fromPosition, int toPosition, int itemCount) {
                    lastSensors= sensors.toArray(lastSensors);
                }

                @Override
                public void onItemRangeRemoved(ObservableList<Sensor> sender, int positionStart, int itemCount) {
                    for(int i=positionStart;i<positionStart+itemCount;i++){
                        Sensor now=lastSensors[i];
                        try {
                            MainApplication.dbService.delSensor(now.getID());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    lastSensors= sensors.toArray(lastSensors);
                }
            });
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
