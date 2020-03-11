package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.service.IDatabaseService;
import com.hit_src.iot_terminal.service.IStatusService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OverviewActivity extends AppCompatActivity {
    /**
     * OverviewActivity是系统概述界面
     * OverviewStatusHandler用于处理界面显示变更
     * onNewIntent是固定的处理活动切换的生命周期函数
     * onCreate是固定的处理活动创建的生命周期函数
     * onResume是固定的处理活动显示的生命周期函数
     */
    private IStatusService statusService;
    private IDatabaseService dbService;
    private int serviceReadyCNT=0;
    private ServiceConnection statusServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            statusService=IStatusService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    updateSensorShow();
                    updateInternetShow();
                    updateOverviewShow();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection dbServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService=IDatabaseService.Stub.asInterface(service);
            synchronized ((Integer)serviceReadyCNT){
                serviceReadyCNT+=1;
                if(serviceReadyCNT>=2){
                    updateSensorShow();
                    updateInternetShow();
                    updateOverviewShow();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private OverviewActivity self;
    private static boolean sensor_status;
    private static boolean internet_status;

    private ListView sensorListListView;
    private ImageView serialStatusImageView;
    private TextView serialStatusTextView;
    private ImageView internetStatusImageView;
    private TextView internetStatusTextView;
    private ImageView overviewStatusImageView;
    private TextView overviewStatusTextView;
    private TextView internetConnectionTextView;
    private TextView internetLasttimeTextView;

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        self=this;

        sensorListListView=findViewById(R.id.Overview_sensorlist_ListView);
        serialStatusImageView=findViewById(R.id.Overview_Serialstatus_ImageView);
        serialStatusTextView=findViewById(R.id.Overview_Serialstatus_Textview);
        internetStatusImageView=findViewById(R.id.Overview_Internetstatus_Imageview);
        internetStatusTextView=findViewById(R.id.Overview_Internetstatus_Textview);
        overviewStatusImageView=findViewById(R.id.Overview_Overviewstatus_ImageView);
        overviewStatusTextView=findViewById(R.id.Overview_OverviewStatus_TextView);
        internetConnectionTextView=findViewById(R.id.Overview_internet_status_TextView);
        internetLasttimeTextView=findViewById(R.id.Overview_internet_status_lasttime_TextView);

        new InternetSettings(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        bindService(new Intent("com.hit_src.iot_terminal.service.IStatusService"),statusServiceConnection,BIND_AUTO_CREATE);
        bindService(new Intent("com.hit_src.iot_terminal.service.IDatabaseService"),dbServiceConnection,BIND_AUTO_CREATE);
    }

    private void updateSensorShow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> statusSensorList= null;
                ArrayList<Sensor> dbSensorList=null;
                try {
                    statusSensorList = (ArrayList<Integer>) statusService.getSensorList();
                    dbSensorList= (ArrayList<Sensor>) dbService.getSensorList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                final SimpleAdapter adapter=SensorListAdapterFactory.product(self,dbSensorList,statusSensorList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorListListView.setAdapter(adapter);
                    }
                });
                final int imageID;
                final int textID;
                if(statusSensorList.isEmpty()){
                    imageID=R.drawable.redlight;
                    textID=R.string.Serialstatus_allbroken;
                    sensor_status=false;
                } else if(statusSensorList.size()<dbSensorList.size()){
                    imageID=R.drawable.yellowlight;
                    textID=R.string.Serialstatus_partbroken;
                    sensor_status=false;
                } else{
                    imageID=R.drawable.greenlight;
                    textID=R.string.Serialstatus_normal;
                    sensor_status=true;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serialStatusImageView.setImageResource(imageID);
                        serialStatusTextView.setText(textID);
                    }
                });
            }
        }).start();
    }
    private void updateInternetShow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean status = false;
                try {
                    status = statusService.getInternetConnectionStatus();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                long lasttime_tmp = 0;
                try {
                    lasttime_tmp = statusService.getInternetConnectionLasttime();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
                final String lasttime = format.format(new Date(lasttime_tmp));
                if(status){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            internetStatusImageView.setImageResource(R.drawable.greenlight);
                            internetStatusTextView.setText(R.string.Internetstatus_normal);
                            internetConnectionTextView.setText(R.string.Internet_connection_normal);
                        }
                    });
                    internet_status=true;
                } else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            internetStatusImageView.setImageResource(R.drawable.redlight);
                            internetStatusTextView.setText(R.string.Internetstatus_broken);
                            internetConnectionTextView.setText(R.string.Internet_connection_failure);
                        }
                    });
                    internet_status=false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        internetLasttimeTextView.setText(lasttime);
                    }
                });
            }
        }).start();
    }
    private void updateOverviewShow(){
        int cnt = 0;
        if (sensor_status) {
            cnt += 1;
        }
        if (internet_status) {
            cnt += 1;
        }
        switch (cnt) {
            case 0:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewStatusImageView.setImageResource(R.drawable.redlight);
                        overviewStatusTextView.setText(R.string.Overview_allbroken);
                    }
                });
                break;
            case 1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewStatusImageView.setImageResource(R.drawable.yellowlight);
                        overviewStatusTextView.setText(R.string.Overview_partbroken);
                    }
                });
                break;
            case 2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewStatusImageView.setImageResource(R.drawable.greenlight);
                        overviewStatusTextView.setText(R.string.Overview_normal);
                    }
                });
                break;
            default:
                Log.w("OverviewStatusHandler", "Not Matched value!");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(statusServiceConnection);
        unbindService(dbServiceConnection);
    }

}