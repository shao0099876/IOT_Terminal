package com.hit_src.iot_terminal.ui.overview.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hit_src.iot_terminal.R;

@SuppressLint("AppCompatCustomView")
public class GuidanceButton extends Button {
    public GuidanceButton(Context context) {
        super(context);
    }

    public GuidanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuidanceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void init(final Fragment targetFragment){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=((FragmentActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.add(R.id.MainFragment,targetFragment);
                transaction.commit();
            }
        });
    }
}
