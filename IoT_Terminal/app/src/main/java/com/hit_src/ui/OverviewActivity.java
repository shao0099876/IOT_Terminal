package com.hit_src.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit_src.iot_terminal.R;

public class OverviewActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1: //serial status update
                    SharedPreferences sp =getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    String status=sp.getString("serial","ERROR!");
                    if(status.equals(getString(R.string.Serialstatus_allbroken))){
                        ImageView imageView=findViewById(R.id.Overview_overviewarea_serialstatuslight_ImageView);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_overviewarea_serialstatus_TextView);
                        textView.setText(R.string.Serialstatus_allbroken);
                    }
            }
            super.handleMessage(msg);
        }
    };
    private void serialStatusUpdate(){
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Message mes=new Message();
                mes.what=1;
                handler.sendMessage(mes);
            }
        });
        t.start();
    }
    private SharedPreferences.OnSharedPreferenceChangeListener profileChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch(key){
                case "serial":
                    serialStatusUpdate();
                    break;
                case "internet":
                    //overviewAreaUpdate();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        serialStatusUpdate();
        SharedPreferences sp=getSharedPreferences("StatusProfile", Activity.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(profileChangeListener);
    }

}
