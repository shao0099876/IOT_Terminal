package com.hit_src.iot_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit_src.iot_terminal.profile.settings.InternetSettings;
import com.hit_src.iot_terminal.service.SerialService;
import com.hit_src.iot_terminal.ui.OverviewActivity;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("serial-HAI");
        System.loadLibrary("network-HAI");
    }
    boolean DEBUG=true;
    boolean runOverview=true;
    boolean runSerialService=false;
    boolean runIntegerSetting=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(runOverview){
            Intent intent=new Intent(this, OverviewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(runSerialService){
            startService(new Intent(this, SerialService.class));
        }
        if(runIntegerSetting){
            new InternetSettings(this);
        }
    }
}
