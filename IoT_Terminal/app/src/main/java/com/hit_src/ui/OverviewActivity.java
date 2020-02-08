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
    private static final int SERIAL_UPDATE=1;
    private static final int INTERNET_UPDATE=2;
    private static final int OVERVIEW_UPDATE=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        uiStatusUpdate(SERIAL_UPDATE);
        uiStatusUpdate(INTERNET_UPDATE);
        uiStatusUpdate(OVERVIEW_UPDATE);
        SharedPreferences sp=getSharedPreferences("StatusProfile", Activity.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(profileChangeListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener profileChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch(key){
                case "serial":
                    uiStatusUpdate(SERIAL_UPDATE);
                    uiStatusUpdate(OVERVIEW_UPDATE);
                    break;
                case "internet":
                    uiStatusUpdate(INTERNET_UPDATE);
                    uiStatusUpdate(OVERVIEW_UPDATE);
                    break;
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
            String status;
            SharedPreferences sp;
            switch (msg.what){
                case SERIAL_UPDATE: //serial status update
                    sp =getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    status=sp.getString("serial","ERROR!");
                    if(status.equals(getString(R.string.Serialstatus_allbroken))){
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_allbroken);
                    }
                    else if(status.equals(getString(R.string.Serialstatus_partbroken))) {
                        ImageView imageView=findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.yellowlight);
                        TextView textView=findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_partbroken);
                    }
                    else if(status.equals(getString(R.string.Serialstatus_normal))) {
                        ImageView imageView = findViewById(R.id.Overview_Serialstatus_ImageView);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView = findViewById(R.id.Overview_Serialstatus_Textview);
                        textView.setText(R.string.Serialstatus_normal);
                    }
                    break;
                case INTERNET_UPDATE:
                    sp=getSharedPreferences("StatusProfile",Activity.MODE_PRIVATE);
                    status=sp.getString("internet","ERROR!");
                    if(status.equals(getString(R.string.Internetstatus_broken))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.redlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_broken);
                    }
                    else if(status.equals(getString(R.string.Internetstatus_normal))){
                        ImageView imageView=findViewById(R.id.Overview_Internetstatus_Imageview);
                        imageView.setImageResource(R.drawable.greenlight);
                        TextView textView=findViewById(R.id.Overview_Internetstatus_Textview);
                        textView.setText(R.string.Internetstatus_normal);
                    }
                    break;
                case OVERVIEW_UPDATE:
                    TextView textView1=findViewById(R.id.Overview_Internetstatus_Textview);
                    TextView textView2= findViewById(R.id.Overview_Serialstatus_Textview);
                    ImageView imageView=findViewById(R.id.Overview_Overviewstatus_ImageView);
                    TextView textView=findViewById(R.id.Overview_OverviewStatus_TextView);
                    int cnt=0;
                    if(textView1.getText().equals(getString(R.string.Internetstatus_normal))){
                        cnt+=1;
                    }
                    if(textView2.getText().equals(getString(R.string.Serialstatus_normal))){
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
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
