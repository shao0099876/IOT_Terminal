package com.hit_src.iot_terminal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    public static MainActivity self;
    public static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }
}
