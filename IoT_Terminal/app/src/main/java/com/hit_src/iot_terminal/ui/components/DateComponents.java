package com.hit_src.iot_terminal.ui.components;

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
public class DateComponents extends TextView {
    private DateComponents self;
    public DateComponents(Context context) {
        super(context);
        self=this;
        init((Activity) context);
    }

    public DateComponents(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        self=this;
        init((Activity) context);
    }

    public DateComponents(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self=this;
        init((Activity) context);
    }
    private void init(final Activity context){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String s=simpleDateFormat.format(new Date());
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        self.setText(s);
                    }
                });
            }
        },10,1000);

    }
    /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String s1=simpleDateFormat.format(new Date());
                simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
                final String s2=simpleDateFormat.format(new Date());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clockTextView.setText(s1);
                        dateTextView.setText(s2);
                    }
                });
            }
        },10,1000);*/
}
