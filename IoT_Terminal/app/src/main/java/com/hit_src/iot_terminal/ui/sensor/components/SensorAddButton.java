package com.hit_src.iot_terminal.ui.sensor.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

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
