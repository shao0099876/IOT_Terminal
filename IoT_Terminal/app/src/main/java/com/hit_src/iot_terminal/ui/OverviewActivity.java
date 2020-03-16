package com.hit_src.iot_terminal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.factory.SensorListAdapterFactory;
import com.hit_src.iot_terminal.object.Sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class OverviewActivity extends AbstractActivity {

    @Override
    protected void runOnBindService(){
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateSensorShow();
                updateInternetShow();
                updateOverviewShow();
            }
        },50,3000);
    }


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
    private static int imageID;
    private static int textID;

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
                if(statusSensorList.size()==dbSensorList.size()){
                    imageID=R.drawable.greenlight;
                    textID=R.string.Serialstatus_normal;
                    sensor_status=true;
                } else if(statusSensorList.isEmpty()){
                    imageID=R.drawable.redlight;
                    textID=R.string.Serialstatus_allbroken;
                    sensor_status=false;
                } else if(statusSensorList.size()<dbSensorList.size()){
                    imageID=R.drawable.yellowlight;
                    textID=R.string.Serialstatus_partbroken;
                    sensor_status=false;
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

    }
}