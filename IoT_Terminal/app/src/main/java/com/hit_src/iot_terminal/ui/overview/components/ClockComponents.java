package com.hit_src.iot_terminal.ui.overview.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("AppCompatCustomView")
public class ClockComponents extends TextView {
    public ClockComponents(Context context) {
        super(context);
        init();
    }

    public ClockComponents(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockComponents(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String s1=simpleDateFormat.format(new Date());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClockComponents.this.setText(s1);
                    }
                });
            }
        },10,1000);
    }
}
