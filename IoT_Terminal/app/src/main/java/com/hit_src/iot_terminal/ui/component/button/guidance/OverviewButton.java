package com.hit_src.iot_terminal.ui.component.button.guidance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.hit_src.iot_terminal.ui.OverviewActivity;

@SuppressLint("AppCompatCustomView")
public class OverviewButton extends Button{

    public OverviewButton(Context context) {
        super(context);
        setListener();
    }

    public OverviewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setListener();
    }

    public OverviewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListener();
    }
    private void setListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), OverviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });
    }

}
