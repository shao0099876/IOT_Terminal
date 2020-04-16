package com.hit_src.iot_terminal.ui.overview.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.hit_src.iot_terminal.R;

public class StatusLinearLayout extends LinearLayout {

    private final int red= R.color.Overview_Status_Red;
    private final int yellow=R.color.Overview_Status_Yellow;
    private final int green=R.color.Overview_Status_Green;

    public StatusLinearLayout(Context context) {
        super(context);
    }

    public StatusLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setGreen(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundColor(getResources().getColor(green));
            }
        });
    }
    public void setYellow(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundColor(getResources().getColor(yellow));
            }
        });
    }
    public void setRed(){
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundColor(getResources().getColor(red));
            }
        });
    }
}
