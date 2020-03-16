package com.hit_src.iot_terminal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hit_src.iot_terminal.R;

public class DrawActivity extends AbstractActivity {

    @Override
    protected void runOnBindService() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
    }


}
