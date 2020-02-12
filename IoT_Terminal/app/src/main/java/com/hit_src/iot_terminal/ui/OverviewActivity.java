package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.db.Database;
import com.hit_src.iot_terminal.object.Sensor;
import com.hit_src.iot_terminal.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {
    private static final int SENSOR_UPDATE=1;
    private static final int INTERNET_UPDATE=2;

    private boolean sensor_status;
    private boolean internet_status;

    private Activity self;

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        self=this;

        SharedPreferences sp=getSharedPreferences("StatusProfile", Activity.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(profileChangeListener);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Tools.guidanceButtonSet(this);

        sensor_status=false;
        internet_status=false;

        uiStatusUpdate(SENSOR_UPDATE);
        uiStatusUpdate(INTERNET_UPDATE);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener profileChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("connected_sensor")){
                uiStatusUpdate(SENSOR_UPDATE);
            }
            else if(key.equals("internet")){
                uiStatusUpdate(INTERNET_UPDATE);
            }
        }
    };

    private void uiStatusUpdate(final int code){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Message mes=new Message();
                mes.what=code;
                handler.sendMessage(mes);
            }
        });
        t.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            SharedPreferences sp;
            switch (msg.what){
                case SENSOR_UPDATE:
                    sp =getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    HashSet<String> sensorIDSet = (HashSet<String>) sp.getStringSet("connected_sensor",null);

                    Database db=new Database();
                    List<Sensor> list=db.getSensorList();
                    List<HashMap<String,Object> >data= new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        HashMap<String,Object> map=new HashMap<>();
                        //map.put("icon",);
                        map.put("name",list.get(i).toString());
                        assert sensorIDSet != null;
                        map.put("status",sensorIDSet.contains(list.get(i).id_toString())?"  已连接":"  未连接");
                        data.add(map);
                    }
                    String[] from={"name","status"};
                    int[] to={R.id.Overview_Sensor_ListviewLayout_name,R.id.Overview_Sensor_ListviewLayout_status};
                    SimpleAdapter adapter=new SimpleAdapter(self,data,R.layout.overview_sensor_status_listview_layout,from,to);

                    ListView sensorListView=findViewById(R.id.Overview_sensorlist_ListView);
                    sensorListView.setAdapter(adapter);

                    assert sensorIDSet != null;
                    if(sensorIDSet.isEmpty()){
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_allbroken);
                        sensor_status=false;
                    }
                    else if(sensorIDSet.size()<list.size()) {
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.yellowlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_partbroken);
                        sensor_status=false;
                    }
                    else if(sensorIDSet.size()==list.size()) {
                        ImageView imageView = findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView = findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_normal);
                        sensor_status=true;
                    }
                    break;
                case INTERNET_UPDATE:
                    sp=getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    String status=sp.getString("internet","ERROR!");
                    if(status.equals(getString(R.string.Internetstatus_broken))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_broken);
                        internet_status=false;
                    }
                    else if(status.equals(getString(R.string.Internetstatus_normal))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_normal);
                        internet_status=true;
                    }
                    break;
            }
            ImageView imageView=findViewById(R.id.Overview_Overviewstatus_ImageView);
            TextView textView=findViewById(R.id.Overview_OverviewStatus_TextView);
            int cnt=0;
            if(sensor_status){
                cnt+=1;
            }
            if(internet_status){
                cnt+=1;
            }
            switch(cnt){
                case 0:
                    imageView.setImageResource(R.drawable.redlight);
                    textView.setText(R.string.Overview_allbroken);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.yellowlight);
                    textView.setText(R.string.Overview_partbroken);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.greenlight);
                    textView.setText(R.string.Overview_normal);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
