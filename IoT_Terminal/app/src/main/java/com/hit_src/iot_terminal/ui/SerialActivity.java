package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.tools.Tools;

public class SerialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Tools.guidanceButtonSet(this);
    }
}
