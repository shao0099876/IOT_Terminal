package com.hit_src.iot_terminal.ui.sensor.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.MainApplication;
import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.object.Sensor;

import java.util.List;

@SuppressLint("AppCompatCustomView")
public class SensorDelButton extends Button {
    public SensorDelButton(Context context) {
        super(context);
        init();
    }

    public SensorDelButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SensorDelButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){

    }
}
