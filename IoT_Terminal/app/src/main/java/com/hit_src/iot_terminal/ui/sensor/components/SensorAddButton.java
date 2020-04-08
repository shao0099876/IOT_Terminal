package com.hit_src.iot_terminal.ui.sensor.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.sensor.SensorAddFragment;

@SuppressLint("AppCompatCustomView")
public class SensorAddButton extends Button {
    public SensorAddButton(Context context) {
        super(context);
        init();
    }

    public SensorAddButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SensorAddButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }
}
