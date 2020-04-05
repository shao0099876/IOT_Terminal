package com.hit_src.iot_terminal.ui.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.R;
import com.hit_src.iot_terminal.ui.sensor.SensorFragment;

@SuppressLint("AppCompatCustomView")
public class SensorButton extends Button {
    public SensorButton(Context context) {
        super(context);
        init((FragmentActivity) context);
    }

    public SensorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init((FragmentActivity) context);
    }

    public SensorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init((FragmentActivity) context);
    }
    private void init(final FragmentActivity context){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=context.getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.add(R.id.MainFragment,new SensorFragment());
                transaction.commit();
            }
        });
    }
}
