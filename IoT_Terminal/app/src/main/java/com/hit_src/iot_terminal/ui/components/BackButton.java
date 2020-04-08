package com.hit_src.iot_terminal.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class BackButton extends Button {
    public BackButton(Context context) {
        super(context);
        init();
    }

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                ArrayList<Fragment> list= (ArrayList<Fragment>) manager.getFragments();
                FragmentTransaction transaction=manager.beginTransaction();
                for(int i=1;i<=list.size()-1;i++){
                    transaction.remove(list.get(list.size()-i));
                }
                transaction.commit();
            }
        });
    }
}
